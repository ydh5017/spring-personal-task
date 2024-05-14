package com.sparta.nbcampspringpersonaltask.exception;

import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException {

    private String result;
    private ErrorCode errorCode;
    private String message;

    public ScheduleException(ErrorCode errorCode) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
