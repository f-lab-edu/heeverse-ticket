package com.heeverse.ticket_order.domain.exception;

/**
 * @author jeongheekim
 * @date 11/20/23
 */
public class TicketOrderException extends RuntimeException {

    public TicketOrderException(Throwable throwable) {
        super(throwable);
    }
}
