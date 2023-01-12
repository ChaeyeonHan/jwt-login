package com.study.jwtlogin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
