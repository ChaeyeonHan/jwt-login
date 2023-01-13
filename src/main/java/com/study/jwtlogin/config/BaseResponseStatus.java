package com.study.jwtlogin.config;


import lombok.AllArgsConstructor;
import lombok.Getter;

// 에러 코드를 관리하는 곳 (enum을 통해 Status를 관리해준다)
@Getter
@AllArgsConstructor
public enum BaseResponseStatus {


    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;

//    private BaseResponseStatus(boolean isSuccess, int code, String message) {
//        this.isSuccess = isSuccess;
//        this.code = code;
//        this.message = message;
//    }

}
