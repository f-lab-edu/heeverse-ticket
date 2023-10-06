package com.heeverse.ticket_order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jeongheekim
 * @date 2023/09/17
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LockOccupancyFailureException extends RuntimeException{
    public LockOccupancyFailureException(String message) {
        super(message);
    }
}
