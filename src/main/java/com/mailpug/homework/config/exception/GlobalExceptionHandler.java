package com.mailpug.homework.config.exception;

import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.common.reponse.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<responseBody, HttpStatus>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException", ex);

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList())
                .get(0);

        final ErrorResponse response = getErrorResponse (errorCode,message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에  누락된 필드가 존재할 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {

        log.error("MissingRequestHeaderException", ex);

        String headerName = ex.getHeaderName();

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_HEADER;

        String message = headerName + errorCode.getMessage();

        final ErrorResponse response = getErrorResponse (errorCode,message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에  유효하지 않은 필드가 있을 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {

        log.error("ConstraintViolationException", ex);

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_HEADER;

        //propertyPath 값 가져오기
        String property = ex.getConstraintViolations()
                .stream()
                .map(x -> x.getPropertyPath().toString())  // 수정된 부분
                .findFirst()  // 첫 번째 프로퍼티 경로만 사용
                .orElse("");

        String message = ex.getMessage();

        if (property.equals("addPost.userId")) {
            message = "X-UERID는 3자에서 10자 사이여야 합니다.";
        }

        final ErrorResponse response = getErrorResponse (errorCode,message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 누락된 값이 있을 경우
     *
     * @param ex IllegalArgumentException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        log.error("IllegalArgumentException", ex);

        String message = ex.getMessage();

        ErrorCode errorCode = ErrorCode.NULL_POINT_ERROR;

        final ErrorResponse response = getErrorResponse (errorCode,message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 누락된 값이 있을 경우
     *
     * @param ex IllegalArgumentException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<ErrorResponse> handleNoResultException(NoResultException ex) {

        log.error("NoResultException", ex);

        ErrorCode errorCode = ErrorCode.NO_MATCHES_FOUND;

        String message = errorCode.getMessage();

        final ErrorResponse response = getErrorResponse (errorCode,message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * [Exception] 비즈니스로직에서 에서 발생한 에러
     *
     * @param ex BusinessExceptionHandler
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BusinessExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessExceptionHandler ex) {

        log.error("BusinessExceptionHandler", ex);

        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        if ("".equals(message)) {
            message = errorCode.getMessage();
        }

        final ErrorResponse response = getErrorResponse (errorCode,message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *  ErrorResponse 로 반환해 주는 메서드
     *
     * @param errorCode ErrorCode
     * @param message String
     *
     * @return ErrorResponse
     */
    private ErrorResponse getErrorResponse(ErrorCode errorCode, String message) {

        return ErrorResponse.builder()
                .status(errorCode.getValue())
                .error(errorCode.getStatus())
                .message(message)
                .build();
    }

}
