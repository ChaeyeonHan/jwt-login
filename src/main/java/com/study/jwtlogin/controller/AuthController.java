package com.study.jwtlogin.controller;

import com.study.jwtlogin.dto.*;
import com.study.jwtlogin.jwt.JwtProperties;
import com.study.jwtlogin.service.AuthService;
import com.study.jwtlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/signup")  // 회원가입 API
    public ResponseEntity<LoginRes> signup(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        LoginRes loginRes = userService.signup(joinRequestDto);
        return new ResponseEntity<>(loginRes, null, HttpStatus.OK);
    }

    @PostMapping("/reissue")  // 토큰 재발급 API
    public void reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        authService.reissue(tokenRequestDto);
    }

}
