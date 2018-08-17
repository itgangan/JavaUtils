package com.collections;

import redis.clients.jedis.Tuple;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DelayQueueBaseRedisSortSet {

    private final String key;
    private final IRedisSortSetService redisService;

    private transient final ReentrantLock lock = new ReentrantLock();
    private transient final Condition available = lock.newCondition();

    public DelayQueueBaseRedisSortSet(String key, IRedisSortSetService redisService) {
        this.key = key;
        this.redisService = redisService;
    }

    public long put(String member, long deadline) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Tuple first = getFirst();
            long r = redisService.zadd(key, deadline, member);
            // 如果当前队列没有元素，或者新加元素<队列第一个元素，唤醒其它线程
            if (first == null || deadline < first.getScore())
                available.signalAll();
            return r;
        } finally {
            lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (true) {
                Tuple first = getFirst();
                if (first == null) {
                    available.await();
                } else {
                    long scores = Math.round(first.getScore());
                    long delayTime = scores - System.currentTimeMillis();
                    if (delayTime > 0) {
                        available.await(delayTime, TimeUnit.MILLISECONDS);
                    } else {
                        String member = first.getElement();
                        redisService.zrem(key, member);
                        if (redisService.zcard(key) != 0) {
                            available.signalAll();
                        }
                        return member;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private Tuple getFirst() {
        Set<Tuple> sets = redisService.zrangeWithScores(key, 0,0);
        if (sets != null && sets.size() > 0) {
            Tuple next = sets.iterator().next();
            return next;
        }
        return null;
    }

}
