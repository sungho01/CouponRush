package com.seongho.couponrush.repository;

import com.seongho.couponrush.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    boolean existsByMember_IdAndCoupon_Id(Long memberId, Long couponId);

    @EntityGraph(attributePaths = "coupon")
    Page<MemberCoupon> findAllByMember_Id(Long memberId, Pageable pageable);
    Optional<MemberCoupon> findByIdAndMember_Id(Long couponId, Long memberId);
    long countByCoupon_Id(Long couponId);
}
