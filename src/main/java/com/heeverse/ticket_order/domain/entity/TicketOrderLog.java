package com.heeverse.ticket_order.domain.entity;

import com.heeverse.common.BaseEntity;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
public class TicketOrderLog extends BaseEntity {

    private final Long ticketSeq;
    private final Long memberSeq;
    private final Long ticketOrderSeq;

    public TicketOrderLog(Long ticketSeq, Long memberSeq, Long ticketOrderSeq) {
        this.ticketSeq = ticketSeq;
        this.memberSeq = memberSeq;
        this.ticketOrderSeq = ticketOrderSeq;
    }
}
