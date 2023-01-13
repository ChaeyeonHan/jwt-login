package com.study.jwtlogin.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.study.jwtlogin.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})  // JSON 직렬화 순서를 제어
public class BaseResponse<T> {   // Response시 공통 부분은 묶고 다른 부분은 제네릭으로 구현해 반복되는 코드를 줄여준다

//    @JsonProperty("isSuccess")  // key를 매핑시켜준다
    private final Boolean isSuccess;
    private final String message;
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // null인 데이터는 json 결과에 나타나지 않는다
    private T result;

    // 요청에 성공한 경우(성공시에만 result를 반환해주고, 실패시에는 result결과값은 나타나지 X)
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }



    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}
