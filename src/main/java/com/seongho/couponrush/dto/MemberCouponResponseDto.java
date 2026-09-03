package com.seongho.couponrush.dto;

import com.seongho.couponrush.enums.MemberCouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberCouponResponseDto {
    private Long id;
    private String couponName;
    private MemberCouponStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
}
