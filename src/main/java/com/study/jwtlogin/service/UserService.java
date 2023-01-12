package com.study.jwtlogin.service;

import com.study.jwtlogin.domain.LoginType;
import com.study.jwtlogin.domain.Role;
import com.study.jwtlogin.domain.User;
import com.study.jwtlogin.dto.JoinRequestDto;
import com.study.jwtlogin.dto.LoginRes;
import com.study.jwtlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginRes signup(JoinRequestDto joinRequestDto) {
        if (userRepository.existsByEmail(joinRequestDto.getEmail())) {
            throw new IllegalStateException("이미 가입되어 있는 유저입니다.");
        }

        userRepository.save(User.builder()
                .email(joinRequestDto.getEmail())
                .password(passwordEncoder.encode(joinRequestDto.getPassword()))
                .loginType(LoginType.FORM)
                .role(Role.ROLE_USER)
                .activated(true).build());

        System.out.println(joinRequestDto.getEmail() + " : 해당 유저 가입 완료");
        return LoginRes.builder()
                .email(joinRequestDto.getEmail()).build();

    }
}
