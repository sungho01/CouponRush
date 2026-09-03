package com.seongho.couponrush.dto;

import com.seongho.couponrush.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birthDate;
}
