package com.thangnt.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thangnt.ddd.application.dto.cache.TicketDetailsCacheDTO;
import com.thangnt.ddd.domain.model.entity.TicketDetails;
import com.thangnt.ddd.domain.repository.TicketDetailsRepository;
import com.thangnt.ddd.domain.service.TicketDetailService;
import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedLocker;
import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedService;
import com.thangnt.ddd.infrastructure.redis.RedisInfraService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailsCacheService {

    RedisInfraService redisInfraService;

    RedissonDistributedService redissonDistributedService;

    TicketDetailService ticketDetailService;

    TicketDetailsRepository ticketDetailsRepository;

    static Cache<Long, TicketDetailsCacheDTO> ticketLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(5)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

//    public boolean orderTicket(Long ticketId) {
//        ticketLocalCache.invalidate(genTicketDetailsLocalCache(ticketId));
//        redisInfraService.delete(genTicketDetailsRedisKey(ticketId));
//        return true;
//    }

        public int orderTicket(long ticketId, int quantity) {
            int deceaseStockCache = decreaseStockTicketDetailsCacheByLUA(ticketId, quantity);
            if(deceaseStockCache != 1){
                log.info("MISSING REDIS CACHE FOR TICKET = {}", ticketId);
                return 0;
            }
            return ticketDetailsRepository.decreaseStockAvailable(ticketId, quantity);
    }

    boolean decreaseStockTicketDetailsCache(long ticketId, int quantity){
        String ticketStockQuantityKey = genTicketDetailsStockQuantityKey(ticketId);
        int stockAvailable = redisInfraService.getInt(ticketStockQuantityKey);
        log.info("STOCK AVAILABLE = {}", stockAvailable);
        if(stockAvailable > quantity){
            log.info("STOCK AVAILABLE > QUANTITY. {}, {}", stockAvailable, quantity);
            redisInfraService.setObject(ticketStockQuantityKey, stockAvailable - quantity);
            return true;
        }
        return false;
    }

    int decreaseStockTicketDetailsCacheByLUA(long ticketId, int quantity){
        String ticketStockQuantityKey = genTicketDetailsStockQuantityKey(ticketId);
        String luaScript =
                "local stock = redis.call('GET', KEYS[1]); " +
                        "if stock == false then return -1 end; " +
                        "stock = tonumber(stock); " +
                        "if (stock >= tonumber(ARGV[1])) then " +
                        "   redis.call('SET', KEYS[1], stock - tonumber(ARGV[1])); " +
                        "   return 1; " +
                        "end; "+
                        "return 0; ";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisInfraService.getRedisTemplate().execute(redisScript, Collections.singletonList(ticketStockQuantityKey), quantity);
        return result != null ? result.intValue(): -1;
        }

    public TicketDetailsCacheDTO getTicketDetails(Long ticketId, Long version) {
        TicketDetailsCacheDTO ticketDetails = getTicketLocalCache(ticketId);
        if (ticketDetails != null) {

            if (version == null || version <= ticketDetails.getVersion()) {
                log.info("GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetails.getVersion());
                return ticketDetails;
            }
        }

        return getTicketFromDistributedCache(ticketId);
    }

    public TicketDetailsCacheDTO getTicketFromDistributedCache(Long ticketId) {
        TicketDetailsCacheDTO ticketCacheDTO = redisInfraService.getObject(genTicketDetailsRedisKey(ticketId), TicketDetailsCacheDTO.class);
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        if (ticketCacheDTO == null) {
            // lock()
            ticketCacheDTO = getTicketDatabase(ticketId);
        }
        ticketLocalCache.put(ticketId, ticketCacheDTO);
        return ticketCacheDTO;
    }


    public TicketDetailsCacheDTO getTicketDatabase(Long ticketId) {

        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock(genTicketDetailsLockKey(ticketId));
        try {
            // wait max 1s to get lock from another thread and wait max 5s before unlock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return redisInfraService.getObject(genTicketDetailsRedisKey(ticketId), TicketDetailsCacheDTO.class);
            }
            TicketDetailsCacheDTO ticketDetails = redisInfraService.getObject(genTicketDetailsRedisKey(ticketId), TicketDetailsCacheDTO.class);
            if (ticketDetails != null) {
                return ticketDetails;
            }

            TicketDetails ticket = ticketDetailService.getById(ticketId);
            ticketDetails = new TicketDetailsCacheDTO().withClone(ticket).withVersion(System.currentTimeMillis());
            redisInfraService.setObject(genTicketDetailsRedisKey(ticketId), ticketDetails);
            return ticketDetails;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            locker.unLock();
        }
        return null;
    }

    private TicketDetailsCacheDTO getTicketLocalCache(Long id) {
        try {
            return ticketLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private String genTicketDetailsRedisKey(Long ticketId) {
        return "REDIS_TICKET_DETAILS_ID_" + ticketId;
    }
    private String genTicketDetailsLocalCache(Long ticketId) {
        return "LOCAL_CACHE_TICKET_DETAILS_ID_" + ticketId;
    }

    private String genTicketDetailsLockKey(Long ticketId) {
        return "LOCK_KEY_TICKET_DETAILS_" + ticketId;
    }
    private String genTicketDetailsStockQuantityKey(Long ticketId) {
        return "REDIS_STOCK_QUANTITY_TICKET_" + ticketId;
    }

}