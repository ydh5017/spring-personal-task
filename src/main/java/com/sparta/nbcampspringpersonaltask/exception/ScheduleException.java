package com.sparta.nbcampspringpersonaltask.exception;

import com.sparta.nbcampspringpersonaltask.enumType.ErrorCode;
import lombok.Getter;

/**
 * 일정 예외
 */
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
