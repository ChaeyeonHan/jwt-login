package com.study.jwtlogin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "refresh_token_key", nullable = false)
    private String key;  // user의 email이 들어간다

   @Column(name = "refresh_token_value", nullable = false)
    private String value;  // 리프레시 토큰 값

}
