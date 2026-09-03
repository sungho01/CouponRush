package com.seongho.couponrush.repository;

import com.seongho.couponrush.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
