package com.seongho.couponrush.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisLockService {
    private final StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 end",
            Long.class
    );

    public String tryLock(Long couponId){
        String key = "lock:coupon:" + couponId;
        String lockValue = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, lockValue, Duration.ofSeconds(10));

        if(Boolean.TRUE.equals(success)){
            return lockValue;
        }

        return null;
    }

    public void unlock(Long couponId, String lockValue){
        String key = "lock:coupon:" + couponId;

        redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(key),
                lockValue
        );
    }

}
