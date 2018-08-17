package com.collections;

import com.xunbao.mall.redis.RedisService;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisListServiceImpl implements IRedisListService {

    private static final RedisService redisService = RedisService.getInstance();



    @Override
    public String blpop(String key) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();

            List<String> blpop = jedis.blpop(0, key);

            return blpop.get(1);

        } finally {
            closeJedis(jedis);
        }

    }

    @Override
    public int rpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = redisService.getConnection();

            Long r = jedis.rpush(key, value);

            return r.intValue();

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
