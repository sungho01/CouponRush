package com.seongho.couponrush.service;

import com.seongho.couponrush.dto.LoginRequestDto;
import com.seongho.couponrush.dto.LoginResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.dto.SignupRequestDto;
import com.seongho.couponrush.entity.Member;
import com.seongho.couponrush.repository.MemberRepository;
import com.seongho.couponrush.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public MessageResponseDto signup(SignupRequestDto requestDto){
        if(memberRepository.existsByLoginId(requestDto.getLoginId())){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if(memberRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        Member member = Member.createUser(
                requestDto.getLoginId(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getBirthDate()
        );

        memberRepository.save(member);

        return new MessageResponseDto(member.getName() + "님 회원가입이 완료되었습니다.");
    }
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto requestDto){
        Member member = memberRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtProvider.createAccessToken(
                member.getLoginId(),
                member.getMemberRole()
        );

        return new LoginResponseDto(accessToken, member.getName(), member.getMemberRole());
    }

    public MessageResponseDto logout(String token){
        long remainingTime = jwtProvider.getRemainingExpiration(token);

        if(remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    "blacklist:" + token,
                    "logout",
                    Duration.ofMillis(remainingTime)
            );
        }
        return new MessageResponseDto("로그아웃 되었습니다.");
    }
}
