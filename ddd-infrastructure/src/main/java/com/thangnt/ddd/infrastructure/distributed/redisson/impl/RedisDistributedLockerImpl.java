package com.thangnt.ddd.infrastructure.distributed.redisson.impl;

import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedLocker;
import com.thangnt.ddd.infrastructure.distributed.redisson.RedissonDistributedService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisDistributedLockerImpl implements RedissonDistributedService {

    @Resource
    RedissonClient redissonClient;

    @Override
    public RedissonDistributedLocker getDistributedLock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);

        return new RedissonDistributedLocker() {
            @Override
            public boolean tryLock(long waitTime, long timeBase, TimeUnit unit) throws InterruptedException {
                boolean isLockSuccess = rLock.tryLock(waitTime, timeBase, unit);
                log.info("{} get lock result: {}", lockKey, isLockSuccess);
                return false;
            }

            @Override
            public void lock(long timeBase, TimeUnit unit) {
                log.info("Locked in {} mls", timeBase);
                rLock.lock(timeBase, unit);
            }

            @Override
            public boolean isLock() {
                return rLock.isLocked();
            }

            @Override
            public void unLock() {
                if(isLock() && isHeldByCurrentThread()){
                    rLock.unlock();
                }
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}
