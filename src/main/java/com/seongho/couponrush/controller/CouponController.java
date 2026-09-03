package com.seongho.couponrush.controller;

import com.seongho.couponrush.dto.CouponCreateRequestDto;
import com.seongho.couponrush.dto.CouponResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;
    @PostMapping
    public MessageResponseDto createCoupon(@RequestBody @Valid CouponCreateRequestDto requestDto){
        return couponService.createCoupon(requestDto);
    }
    @GetMapping
    public Page<CouponResponseDto> getCoupons(Pageable pageable){
        return couponService.getCoupons(pageable);
    }
    @GetMapping("/{couponId}")
    public CouponResponseDto getCoupon(@PathVariable Long couponId){
        return  couponService.getCoupon(couponId);
    }
}
