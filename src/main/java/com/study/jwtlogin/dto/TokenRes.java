package com.study.jwtlogin.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRes {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
