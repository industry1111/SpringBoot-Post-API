package com.mailpug.homework.config.exception;

import com.mailpug.homework.common.codes.ErrorCode;
import lombok.Builder;
import lombok.Getter;

/**
 * 비즈니스 로직 에러를 사용하기 위한 구현체
 */
public class BusinessExceptionHandler extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
