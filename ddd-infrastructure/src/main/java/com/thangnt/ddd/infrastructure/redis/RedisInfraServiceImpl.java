package com.thangnt.ddd.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class RedisInfraServiceImpl implements RedisInfraService{

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if(!StringUtils.hasLength(key)){
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key, String value) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if(!StringUtils.hasLength(key)){
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> T getObject(String key, Class<T> targetValueType) {
        Object result = redisTemplate.opsForValue().get(key);
        log.info("Cache value = {}", result);
        if(result == null) {
            return null;
        }

        if(targetValueType.isInstance(result)){
            return targetValueType.cast(result);
        }

        ObjectMapper mapper = new ObjectMapper();
        if(result instanceof Map){
            try {
                return mapper.convertValue(result, targetValueType);
            }catch (IllegalArgumentException e){
                log.error(e.getMessage());
                return null;
            }
        }

        if(result instanceof String){
            try {
                return mapper.readValue((String) result, targetValueType);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return  null;
    }
}
