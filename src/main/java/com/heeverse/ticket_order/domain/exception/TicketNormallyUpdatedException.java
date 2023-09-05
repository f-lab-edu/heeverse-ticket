package com.heeverse.ticket_order.domain.exception;

/**
 * @author jeongheekim
 * @date 2023/09/05
 */
public class TicketNormallyUpdatedException extends RuntimeException {
    public TicketNormallyUpdatedException(String message) {
        super(message);
    }
}
