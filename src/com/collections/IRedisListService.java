package com.collections;

public interface IRedisListService {


    String blpop(String key);

    int rpush(String key, String value);

}
