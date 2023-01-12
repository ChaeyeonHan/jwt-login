package com.study.jwtlogin.controller;

import com.study.jwtlogin.dto.JoinRequestDto;
import com.study.jwtlogin.dto.LoginRequestDto;
import com.study.jwtlogin.dto.TokenRequestDto;
import com.study.jwtlogin.service.AuthService;
import com.study.jwtlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/signup")  // 회원가입 API
    public void signup(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        userService.signup(joinRequestDto);
    }


    @PostMapping("/reissue")  // 토큰 재발급 API
    public void reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        authService.reissue(tokenRequestDto);
    }

}
