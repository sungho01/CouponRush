package com.seongho.couponrush.scheduler;

import com.seongho.couponrush.entity.Coupon;
import com.seongho.couponrush.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponScheduler {
    private  final CouponRepository couponRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateCouponStatus(){
        List<Coupon> coupons = couponRepository.findAll();

        for(Coupon coupon : coupons){
            coupon.updateStatus();
        }
    }
}
