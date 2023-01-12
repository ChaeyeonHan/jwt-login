package com.study.jwtlogin.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TokenProvider와 JwtFilter를 SecurityConfig에 적용할 때 사용한다

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        JwtFilter customFilter = new JwtFilter(tokenProvider);

        // 직접 만든 JwtFilter를 시큐리티 필터 앞에 추가해준다
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
