package com.heeverse.ticket_order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jeongheekim
 * @date 2023/09/02
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyBookedTicketException extends RuntimeException {
    public AlreadyBookedTicketException(String message) {
        super(message);
    }
}
