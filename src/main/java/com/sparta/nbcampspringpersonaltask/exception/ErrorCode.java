package com.sparta.nbcampspringpersonaltask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "선택한 일정은 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
