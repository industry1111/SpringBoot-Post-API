package com.mailpug.homework.common.reponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [공통] API Error Response 결과의 반환 값을 관리
 */
@ApiModel(description = "ErrorResponse")
@Schema(description = "Error 발생시 응답 값")
@Data
public class ErrorResponse {

    @Schema(description = "에러 발생 시간")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime dateTime = LocalDateTime.now();

    @Schema(description = "HTTP 상태 코드")
    private final int status;

    @Schema(description = "에러 코드")
    private final String error;

    @Schema(description = "에러 메세지")
    private final String message;

    @Builder
    public ErrorResponse(int status, String error, String message ) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}