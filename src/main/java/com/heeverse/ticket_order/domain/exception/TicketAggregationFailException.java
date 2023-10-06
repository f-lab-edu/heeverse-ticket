package com.heeverse.ticket_order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gutenlee
 * @since 2023/09/23
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketAggregationFailException extends RuntimeException {
    public TicketAggregationFailException(Throwable cause) {
        super.initCause(cause);
    }
}
