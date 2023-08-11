package com.mailpug.homework.common.reponse;


import lombok.Builder;
import lombok.Getter;

/**
 * [공통] API Response 결과의 반환 값을 관리
 */
@Getter
public class ApiResponse<T> {

    //API 응답 결과
    private T result;

    //API 응답 코드
    private int resultCode;

    //API 응답 코드
    private String resultMsg;

    @Builder
    public ApiResponse(T result, int resultCode, String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
