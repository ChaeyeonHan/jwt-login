package com.study.jwtlogin.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class LoginRequestDto {
    private String email;

    private String password;

    // 로그인한 정보를 기반으로 AuthenticationToken 생성
    public UsernamePasswordAuthenticationToken toAuthenticate() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
