package com.heeverse.ticket_order.domain.exception;

/**
 * @author jeongheekim
 * @date 2023/09/05
 */
public class TicketNotNormallyUpdatedException extends RuntimeException {
    public TicketNotNormallyUpdatedException(Throwable cause) {
        super(cause);
    }

    public TicketNotNormallyUpdatedException(String message) {
        super(message);
    }
}
