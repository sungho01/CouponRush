package com.seongho.couponrush.controller;

import com.seongho.couponrush.dto.MemberCouponResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-coupons")
public class MemberCouponController {
    private final MemberCouponService memberCouponService;

    @PostMapping("/issue/{couponId}")
    public MessageResponseDto issueCoupon(Authentication authentication,@PathVariable Long couponId){
        String loginId = authentication.getName();

        return memberCouponService.issueCoupon(loginId, couponId);
    }

    @GetMapping
    public Page<MemberCouponResponseDto> getMyCoupons(Authentication authentication, Pageable pageable){
        String loginId = authentication.getName();

        return memberCouponService.getMyCoupons(loginId, pageable);
    }
    @GetMapping("/{memberCouponId}")
    public MemberCouponResponseDto getMyCoupon(Authentication authentication,@PathVariable Long memberCouponId){
        String loginId = authentication.getName();

        return memberCouponService.getMyCoupon(loginId, memberCouponId);
    }

    @PatchMapping("/{memberCouponId}/use")
    public MessageResponseDto useCoupon(Authentication authentication,@PathVariable Long memberCouponId){
        String loginId = authentication.getName();

        return memberCouponService.useCoupon(loginId, memberCouponId);
    }
}
