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
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException", ex);

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        responseBody.put("error", errors.get(0));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에  X-USERID 필드 누락
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());

        responseBody.put("error", "X-USERID 는 필수 입니다.");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에  X-USERID 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException", ex);

        //propertyPath 값 가져오기
        String property = ex.getConstraintViolations()
                .stream()
                .map(x -> x.getPropertyPath().toString())  // 수정된 부분
                .findFirst()  // 첫 번째 프로퍼티 경로만 사용
                .orElse("");

        String error = "";

        if (property.equals("addPost.userId")) {
            error = "X-UERID는 3자에서 10자 사이여야 합니다.";
        }

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", error);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * [Exception] 비즈니스로직에서 에서 발생한 에러
     *
     * @param ex BusinessExceptionHandler
     * @return ResponseEntity
     */
    @ExceptionHandler(BusinessExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessExceptionHandler ex) {

        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        if ("".equals(message)) {
            message = errorCode.getMessage();
        }

        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getValue())
                .error(errorCode.getStatus())
                .message(message)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
