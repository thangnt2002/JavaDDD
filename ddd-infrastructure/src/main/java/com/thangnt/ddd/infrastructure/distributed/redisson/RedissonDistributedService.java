package com.thangnt.ddd.infrastructure.distributed.redisson;

public interface RedissonDistributedService {
    RedissonDistributedLocker getDistributedLock(String lockKey);

}
