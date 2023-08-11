package com.mailpug.homework.common.codes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * [공통 코드] API 통신에 대한 '실패 코드'를 Enum 형태로 관리를 한다.
 */
@Getter
public enum ErrorCode {

    // Client
    BAD_REQUEST(400,"G001", "잘못된 요청 입니다."),
    INVALID_INPUT_ERROR(400,"G002", "Input 값 오류 입니다."),
    INVALID_INPUT_HEADER(400,"G003", ""),

    FORBIDDEN_ERROR(403,"G005","권한이 없습니다."),

    NO_HANDLER_FOUND(404,"G005", "존재하지 않는 API"),
    NULL_POINT_ERROR(404,"G006", "Null Point Exception");

    private final int value;
    private final String status;
    private final String message;

    ErrorCode(int value, String status, String message) {
        this.value = value;
        this.status = status;
        this.message = message;
    }
}
