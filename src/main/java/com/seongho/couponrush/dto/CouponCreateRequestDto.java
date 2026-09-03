package com.seongho.couponrush.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequestDto {
    @NotBlank(message = "쿠폰 이름을 입력해주세요.")
    private String name;
    @Positive(message = "쿠폰 수량은 1개 이상이어야 합니다.")
    @NotNull(message = "쿠폰 수량을 입력해주세요.")
    private Integer totalQuantity;
    @NotNull(message = "시작일을 입력해주세요.")
    private LocalDate startDate;
    @NotNull(message = "종료일을 입력해주세요.")
    private LocalDate endDate;
}
