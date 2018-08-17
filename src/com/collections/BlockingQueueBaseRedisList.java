package com.collections;

/**
 * 基于redis的无界阻塞队列（不可靠，消息取出后，在处理消息的过程中，如果程序崩溃或者网络异常，消息会丢失）
 */
public class BlockingQueueBaseRedisList {

    private final String key;
    private final IRedisListService redisService;

    public BlockingQueueBaseRedisList(String key, IRedisListService redisService) {
        this.key = key;
        this.redisService = redisService;
    }


    public int put(String value) {

        return redisService.rpush(key, value);
    }

    public String take() {

        return redisService.blpop(key);
    }


}
