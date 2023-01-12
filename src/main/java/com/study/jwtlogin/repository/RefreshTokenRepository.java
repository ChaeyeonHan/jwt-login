package com.study.jwtlogin.repository;

import com.study.jwtlogin.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKey(String key);  // email로 토큰 존재하는지 확인
}
