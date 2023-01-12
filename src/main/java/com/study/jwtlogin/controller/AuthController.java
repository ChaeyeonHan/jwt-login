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

    @GetMapping("/login")  // 로그인 API
    public ResponseEntity<TokenRes> formLogin(@RequestBody LoginRequestDto loginDto) {
        System.out.println("로그인 요청");
        // 로그인 요청이 들어오면, loginDto로 받아온 email, password로 사용자 인증을 진행하고
        // UserPasswordAuthenticationToken 객체를 생성하고, password가 일치하는지 확인
        // 검증이 정상적으로 진행되었다면, JWT 토큰을 생성(accessToken, refreshToken 모두 발급)하고 저장한다
        TokenRes res = authService.login(loginDto);
//        HttpHeaders header = new HttpHeaders();
//        header.add(JwtProperties.AUTHORIZATION_HEADER, "Bearer " + res.getAccessToken());
        System.out.println("authService.login");
        return new ResponseEntity<>(res,null, HttpStatus.OK);  // 바디, 헤더, 상태코드
    }

    @PostMapping("/reissue")  // 토큰 재발급 API
    public void reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        authService.reissue(tokenRequestDto);
    }

}
