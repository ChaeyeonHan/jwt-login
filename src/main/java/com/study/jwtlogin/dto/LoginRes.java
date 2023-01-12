package com.study.jwtlogin.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {
    private String email;
}
