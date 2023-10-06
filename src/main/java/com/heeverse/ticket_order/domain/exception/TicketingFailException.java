package com.heeverse.ticket_order.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jeongheekim
 * @date 2023/09/12
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TicketingFailException extends RuntimeException {

    public TicketingFailException(Throwable cause) {
        super.initCause(cause);
    }

}
