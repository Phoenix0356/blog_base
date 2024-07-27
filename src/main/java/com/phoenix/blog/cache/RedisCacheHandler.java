package com.phoenix.blog.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheHandler {
//    private final String LOCK_KEY = "Lock";
//    private final String LOCK_VALUE = "val";

    final RedisTemplate<String,Object> redisTemplate;

    public void setCache(String id, Object object){
        redisTemplate.opsForValue().set(id,object);
    }
    public boolean setIfAbsent(String id,Object object){
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(id, object));
    }

    public Object getCache(String Id, Class<?> clazz){
        return new ObjectMapper().convertValue(redisTemplate.opsForValue().get(Id),clazz);
    }

    public void deleteCache(String id){
        if (redisTemplate.opsForValue().get(id) == null) return;
        redisTemplate.delete(id);
    }

    //尝试获取锁
//    public void tryLock(){
//        Boolean result = false;
//
//        while (Boolean.FALSE.equals(result)) {
//            result = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, LOCK_VALUE);
//            if (Boolean.FALSE.equals(result)) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
}
