package com.seongho.couponrush.controller;

import com.seongho.couponrush.dto.LoginRequestDto;
import com.seongho.couponrush.dto.LoginResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.dto.SignupRequestDto;
import com.seongho.couponrush.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public MessageResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto){
        return authService.signup(requestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto requestDto){
        return authService.login(requestDto);
    }

    @PostMapping("/logout")
    public MessageResponseDto logout(@RequestHeader("Authorization") String authorization){
        if(!authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더입니다.");
        }
        String token = authorization.split(" ", 2)[1];

        return authService.logout(token);
    }
}
