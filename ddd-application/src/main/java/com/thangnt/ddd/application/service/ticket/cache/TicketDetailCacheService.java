package com.thangnt.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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

    static Cache<Long, Ticket> ticketLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(5)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public Ticket getTicketDefaultCache(Long ticketId, Long version) {
        Ticket ticketDetails = getTicketLocalCache(ticketId);
        if (ticketDetails != null) {
            return ticketDetails;
        }

        ticketDetails = redisInfraService.getObject(genTicketRedisKey(ticketId), Ticket.class);

        if (ticketDetails != null) {
            ticketLocalCache.put(ticketId, ticketDetails);
            return ticketDetails;
        }

        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock("GET_TICKET_BY_ID_LOCK_KEY_" + ticketId);
        try {
            // wait max 1s to get lock from another thread and wait max 5s before unlock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return redisInfraService.getObject(genTicketRedisKey(ticketId), Ticket.class);
            }
            ticketDetails = redisInfraService.getObject(genTicketRedisKey(ticketId), Ticket.class);
            if (ticketDetails != null) {
                return ticketDetails;
            }
            ticketDetails = ticketDetailService.getById(ticketId);
            log.info("FROM DBS => {}, {}", ticketDetails, version);
            log.info("Ticket not exist ...... {}", version);
            redisInfraService.setObject(genTicketRedisKey(ticketId), ticketDetails);
            ticketLocalCache.put(ticketId, ticketDetails);
            return ticketDetails;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            locker.unLock();
        }
        return null;
    }

    private Ticket getTicketLocalCache(Long id){
        try{
            return ticketLocalCache.getIfPresent(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private String genTicketRedisKey(Long ticketId){
        return "GET_TICKET_ID_EVENT_"+ ticketId;
    }
}