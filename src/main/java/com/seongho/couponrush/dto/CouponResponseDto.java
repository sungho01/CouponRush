package com.seongho.couponrush.dto;

import com.seongho.couponrush.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponResponseDto {
    private Long id;
    private String name;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private CouponStatus status;
    private LocalDateTime createdAt;
}
