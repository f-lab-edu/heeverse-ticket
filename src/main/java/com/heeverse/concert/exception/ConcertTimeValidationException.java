package com.heeverse.concert.exception;

/**
 * @author jeongheekim
 * @date 2023/08/08
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConcertTimeValidationException extends RuntimeException{

    public ConcertTimeValidationException(String message) {
        super(message);
    }
}
