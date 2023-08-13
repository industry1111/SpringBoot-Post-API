package com.mailpug.homework.common.reponse;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * [공통] API Response 결과의 반환 값을 관리
 */
@Schema(description = "API 결과 반환")
@Getter
public class CustomApiResponse<T> {

    //API 응답 결과
    @Schema(description = "API 응답 결과")
    private final T result;

    //API 응답 코드
    @Schema(description = "API 응답 코드")
    private final int resultCode;

    //API 응답 메세지
    @Schema(description = "API 응답 메세지")
    private final String resultMsg;

    @Builder
    public CustomApiResponse(T result, int resultCode, String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}