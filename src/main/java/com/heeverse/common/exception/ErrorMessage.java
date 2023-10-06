package com.heeverse.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

    CLIENT_ERROR("요청의 문법이 잘못되었거나 요청을 처리할 수 없음"),
    SERVER_ERROR("서버가 명백히 유효한 요청에 대해 충족을 실패함"),
    UNDEFINED_ERROR("비정상 처리")
    ;

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public static String convert(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            return CLIENT_ERROR.message;
        } else if (httpStatus.is5xxServerError()) {
            return SERVER_ERROR.message;
        }
        return UNDEFINED_ERROR.message;
    }

}
