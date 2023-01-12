package com.study.jwtlogin.dto;


import lombok.*;

@Getter @Setter@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRes {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
