package com.heeverse.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gutenlee
 * @since 2023/08/11
 */
@ResponseStatus(code = HttpStatus.CONFLICT)

public class SerialNumberException extends RuntimeException {

    private static final String MESSAGE = "티켓 시리얼넘버 생성 인자가 적절하지 않습니다";

    public SerialNumberException(Exception e) {
        super(MESSAGE);
    }
}
