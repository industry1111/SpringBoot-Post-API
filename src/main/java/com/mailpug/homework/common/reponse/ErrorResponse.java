package com.mailpug.homework.common.reponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [공통] API Error Response 결과의 반환 값을 관리
 */
@Data
public class ErrorResponse {

    private final LocalDateTime dateTime = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;

    @Builder
    public ErrorResponse(int status, String error, String message ) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}