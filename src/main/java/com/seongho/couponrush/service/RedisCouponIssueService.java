package com.seongho.couponrush.service;

import com.seongho.couponrush.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCouponIssueService {
    private final RedisLockService redisLockService;
    private final MemberCouponService memberCouponService;

    public MessageResponseDto issueCouponWithRedisLock(String loginId, Long couponId){
        String lockValue = null;

        try{
            while(lockValue == null){
                lockValue = redisLockService.tryLock(couponId);

                if(lockValue == null){
                    Thread.sleep(1);
                }
            }
            return  memberCouponService.issueCoupon(loginId, couponId);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();;
            throw new RuntimeException("락 대기 중 인터럽트가 발생했습니다.");
        } finally {
            if(lockValue != null){
                redisLockService.unlock(couponId, lockValue);
            }
        }
    }

}
