package com.study.jwtlogin.jwt;

public interface JwtProperties {
    String BEARER_PREFIX = "Bearer ";
    String AUTHORIZATION_HEADER = "Authorization";
    long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;  // 엑세스 토큰 만료 기한 : 1일
    long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 리프레시 토큰 만료 기한 : 7일
}
