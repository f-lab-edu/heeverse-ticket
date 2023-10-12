package com.heeverse.common.exception;

import org.springframework.http.HttpStatus;

import static com.heeverse.common.util.HttpStatusUtil.*;
import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {

    AUTH_ERROR("적절한 인증 과정 없이 API를 호출했습니다"),
    CLIENT_ERROR("요청의 문법이 잘못되었거나 요청을 처리할 수 없음"),
    SERVER_ERROR("서버가 명백히 유효한 요청에 대해 충족을 실패함"),
    UNDEFINED_ERROR("비정상 처리로 실패함. 문의 바람")
    ;

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public static String convert(HttpStatus httpStatus) {
        if (isNull(httpStatus) || !isError(httpStatus)){
            return UNDEFINED_ERROR.message;
        }

        if (httpStatus.equals(UNAUTHORIZED)){
            return AUTH_ERROR.message;
        }
        if (httpStatus.is4xxClientError()) {
            return CLIENT_ERROR.message;
        }
        return SERVER_ERROR.message;
    }

}
