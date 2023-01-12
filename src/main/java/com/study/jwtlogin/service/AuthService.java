package com.study.jwtlogin.service;

import com.study.jwtlogin.domain.RefreshToken;
import com.study.jwtlogin.dto.TokenRequestDto;
import com.study.jwtlogin.dto.TokenRes;
import com.study.jwtlogin.jwt.TokenProvider;
import com.study.jwtlogin.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional  // 토큰 만료시 재발급
    public TokenRes reissue(TokenRequestDto tokenRequestDto) {

        // 1. Refresh Token 만료 여부 검사
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }
        // 2. Access Token 에서 유저 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        System.out.println(authentication.getName());

        // 3. RefreshTokenRepository에서 email 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token이 일치하는지 검사
        // Access Token을 복호화해서 유저 정보를 가져오고, 저장소에 있는 Refresh Token과 클라이언트가 전달한 Refresh Token의 일치여부 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.(즉, 리프레시 토큰 값이 맞지 않습니다.");
        }

        // 5. 새로운 토큰 발급
        TokenRes tokenRes = tokenProvider.createToken(authentication.getName());

        // 6. 정보 업데이트 후, 새로 발급한 토큰 리턴해주기
        RefreshToken newRefreshToken = refreshToken.updateToken(tokenRes.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenRes;
    }
}
