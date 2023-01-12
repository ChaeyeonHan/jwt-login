package com.study.jwtlogin.controller;

import com.study.jwtlogin.dto.JoinRequestDto;
import com.study.jwtlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")  // 회원가입 API
    public void signup(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        userService.signup(joinRequestDto);
    }

}
