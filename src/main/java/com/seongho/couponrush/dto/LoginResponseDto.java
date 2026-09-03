package com.seongho.couponrush.dto;

import com.seongho.couponrush.enums.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String name;
    private MemberRole role;
}
