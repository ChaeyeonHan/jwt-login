package com.study.jwtlogin.jwt;

import com.study.jwtlogin.dto.TokenRes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.study.jwtlogin.domain.Role.ROLE_USER;

// JWT 토큰 암호화, 복호화, 유효성 검증을 하는 역할

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private Key secretKey;

    //시크릿 값을 decode해서 secretKey 변수에 할당
    public TokenProvider(@Value("${jwt.secret}") String jwtSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 발급(TokenRes 반환)
    public TokenRes createToken(String email) {
        // 엑세스 토큰의 만료 시간 설정
        Date now = new Date();
        Date accessTokenExpireTime = new Date(now.getTime() + JwtProperties.ACCESS_TOKEN_EXPIRE_TIME);

        return TokenRes.builder()
                .grantType(JwtProperties.BEARER_PREFIX)  // JWT 대한 인증 타입 : Bearer
                .accessToken(publishAccessToken(email))
                .accessTokenExpiresIn(accessTokenExpireTime.getTime())
                .refreshToken(publishRefreshToken()).build();
    }

    // Access Token 생성
    public String publishAccessToken(String email) {
        Date now = new Date();
        return Jwts.builder()
//                .setHeaderParam("type", "jwt")
                .setSubject(email)
                .claim(AUTHORITIES_KEY, ROLE_USER)
//                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.ACCESS_TOKEN_EXPIRE_TIME))  // 토큰 만료 시간 설정(1000이 1초)
//                .signWith(SignatureAlgorithm.HS256, JwtProperties.SECRET)  // 내 서버만 아는 고유한 값
                // -> signWith가 deprecated되어 String 값을 넣는 것이 아니라 Key를 생성해 서명을 진행
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Refresh Token 생성
    public String publishRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + JwtProperties.REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {  // 토큰 파싱 후 발생하는 Exception을 종류에 따라 Catch!
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);   // Jwts 모듈이 알아서 Exception 을 던져줌
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보로 Authentication 객체를 리턴하는 메서드
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // Claim 으로 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보 넣지 않고 Authentication 객체 생성해 리턴하는게 맞나?
        // UserDetails 객체를 생성해서 UsernamePasswordAuthenticationToken로 리턴(SecurityContext가 Authentication 객체를 저장하기에)
        // 디비를 거치지 않고 토큰에서 값을 꺼내 바로 시큐리티 유저 객체를 만들어 Authentication을 만들어 반환
        User principal = new User(claims.getSubject(), "", authorities);  // UserDetails 객체를 생성 -> UsernamePasswordAuthenticationToken 형태로 리턴

        return new UsernamePasswordAuthenticationToken(principal,"",authorities);
    }

    // 토큰을 파라미터로 받아 클레임 생성 (토큰의 만료 여부와 상관없이 정보를 꺼낼 수 있음)
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    // 토큰에서 이메일 가져오기


}
