package com.collections;

import com.xunbao.mall.redis.RedisService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class RedisSortSetServiceImpl implements IRedisSortSetService {

    private static final RedisService redisService = RedisService.getInstance();

    @Override
    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();
            return jedis.zadd(key, score, value);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();
            return jedis.zcard(key);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public long zcount(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();
            return jedis.zcount(key, min, max);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();
            return jedis.zrange(key, start, end);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        try{
            jedis = redisService.getConnection();
            return jedis.zrangeWithScores(key, start, end);
        }finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count){
        Jedis jedis = null;
        try{
            jedis = redisService.getConnection();
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        }finally {
            closeJedis(jedis);
        }
    }

    @Override
    public long zrem(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();
            return jedis.zrem(key, member);
        } finally {
            closeJedis(jedis);
        }
    }


    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
