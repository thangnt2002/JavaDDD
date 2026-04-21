package com.thangnt.ddd.infrastructure.distributed.redisson;

import java.util.concurrent.TimeUnit;

public interface RedissonDistributedLocker {

    boolean tryLock(long waitTime, long timeBase, TimeUnit unit) throws InterruptedException;
    void lock(long timeBase, TimeUnit unit);
    boolean isLock();
    void unLock();
    boolean isHeldByThread(long threadId);
    boolean isHeldByCurrentThread();
}
