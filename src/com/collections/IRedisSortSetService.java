package com.collections;

import redis.clients.jedis.Tuple;

import java.util.Set;

public interface IRedisSortSetService {

    long zadd(String key, double score, String value);

    long zcard(String key);

    long zcount(String key, double min, double max);

    Set<String> zrange(String key, long start, long end);

    Set<Tuple> zrangeWithScores(String key, long start, long end);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count);

    long zrem(String key, String value);


}
