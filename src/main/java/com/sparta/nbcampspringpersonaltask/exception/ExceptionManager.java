package com.sparta.nbcampspringpersonaltask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<?> handleException(ScheduleException e){
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ExceptionDto(e.getErrorCode()));
    }
}
