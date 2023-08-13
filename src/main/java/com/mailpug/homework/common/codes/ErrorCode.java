package com.mailpug.homework.common.codes;

import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * [공통 코드] API 통신에 대한 '실패 코드'를 Enum 형태로 관리를 한다.
 */
@Getter
public enum ErrorCode {

    // Client
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "G001", "잘못된 요청 입니다."),
    INVALID_INPUT_HEADER(HttpStatus.BAD_REQUEST.value(),"G002", " 값은 필수 입니다."),

    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN.value(), "G003","권한이 없습니다."),

    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND.value(),"G004", "존재하지 않는 API"),
    NULL_POINT_ERROR(HttpStatus.NOT_FOUND.value(),"G005", "Null Point Exception"),
    NO_MATCHES_FOUND(HttpStatus.NOT_FOUND.value(),"G006", "데이터가 존재하지 않습니다.");

    private final int value;
    private final String status;
    private final String message;

    ErrorCode(int value, String status, String message) {
        this.value = value;
        this.status = status;
        this.message = message;
    }
}
