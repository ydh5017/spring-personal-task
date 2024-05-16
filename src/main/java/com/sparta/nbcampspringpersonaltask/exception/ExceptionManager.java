package com.sparta.nbcampspringpersonaltask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외처리 핸들러 클래스
 */
@RestControllerAdvice
public class ExceptionManager {

    /**
     * 일정 예외처리 핸들러
     * @param e ScheduleException
     * @return 예외 상태코드, 메시지
     */
    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<?> handleException(ScheduleException e){
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ExceptionDto(e.getErrorCode()));
    }

    /**
     * 뭐였더라
     * @param e MethodArgumentNotValidException
     * @return 예외 상태코드, 메시지
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e){
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(builder.toString()));
    }
}
