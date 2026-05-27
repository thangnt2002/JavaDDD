package com.thangnt.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thangnt.ddd.application.dto.cache.TicketCacheDTO;
import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.domain.service.TicketDetailService;
import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedLocker;
import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedService;
import com.thangnt.ddd.infrastructure.redis.RedisInfraService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailCacheService {

    RedisInfraService redisInfraService;

    RedissonDistributedService redissonDistributedService;

    TicketDetailService ticketDetailService;

    static Cache<Long, TicketCacheDTO> ticketLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(5)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public boolean orderTicket(Long ticketId) {
        ticketLocalCache.invalidate(ticketId);
        redisInfraService.delete(genTicketRedisKey(ticketId));
        return true;
    }

    public TicketCacheDTO getTicket(Long ticketId, Long version) {
        TicketCacheDTO ticketDetails = getTicketLocalCache(ticketId);
        if (ticketDetails != null) {

            if (version == null || version <= ticketDetails.getVersion()) {
                log.info("GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetails.getVersion());
                return ticketDetails;
            }
        }

        return getTicketFromDistributedCache(ticketId);
    }

    public TicketCacheDTO getTicketFromDistributedCache(Long ticketId) {
        TicketCacheDTO ticketCacheDTO = redisInfraService.getObject(genTicketRedisKey(ticketId), TicketCacheDTO.class);
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        if (ticketCacheDTO == null) {
            // lock()
            ticketCacheDTO = getTicketDatabase(ticketId);
        }
        ticketLocalCache.put(ticketId, ticketCacheDTO);
        return ticketCacheDTO;
    }


    public TicketCacheDTO getTicketDatabase(Long ticketId) {

        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock("GET_TICKET_BY_ID_LOCK_KEY_" + ticketId);
        try {
            // wait max 1s to get lock from another thread and wait max 5s before unlock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return redisInfraService.getObject(genTicketRedisKey(ticketId), TicketCacheDTO.class);
            }
            TicketCacheDTO ticketDetails = redisInfraService.getObject(genTicketRedisKey(ticketId), TicketCacheDTO.class);
            if (ticketDetails != null) {
                return ticketDetails;
            }

            Ticket ticket = ticketDetailService.getById(ticketId);
            ticketDetails = new TicketCacheDTO().withClone(ticket).withVersion(System.currentTimeMillis());
            redisInfraService.setObject(genTicketRedisKey(ticketId), ticketDetails);
            return ticketDetails;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            locker.unLock();
        }
        return null;
    }

    private TicketCacheDTO getTicketLocalCache(Long id) {
        try {
            return ticketLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String genTicketRedisKey(Long ticketId) {
        return "GET_TICKET_ID_EVENT_" + ticketId;
    }
}