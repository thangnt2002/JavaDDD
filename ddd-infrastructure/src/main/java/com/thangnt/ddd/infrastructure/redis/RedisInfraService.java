package com.thangnt.ddd.infrastructure.redis;

public interface RedisInfraService {
    void setString(String key, String value);
    String getString(String key, String value);
    void setObject(String key, Object value);
    <T> T getObject(String key, Class<T> value);
    void delete(String key);
}
