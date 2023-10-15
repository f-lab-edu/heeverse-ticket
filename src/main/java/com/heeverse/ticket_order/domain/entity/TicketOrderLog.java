package com.heeverse.ticket_order.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.domain.entity.Ticket;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
public class TicketOrderLog extends BaseEntity {

    private final long ticketSeq;
    private final long memberSeq;
    private final long ticketOrderSeq;
    private final long concertSeq;
    private String gradeName;

    public TicketOrderLog(long ticketSeq, long memberSeq, long ticketOrderSeq, long concertSeq) {
        this.ticketSeq = ticketSeq;
        this.memberSeq = memberSeq;
        this.ticketOrderSeq = ticketOrderSeq;
        this.concertSeq = concertSeq;
    }

    public TicketOrderLog(Ticket ticket, Long memberSeq, Long ticketOrderSeq) {
        this.ticketSeq = ticket.getSeq();
        this.memberSeq = memberSeq;
        this.ticketOrderSeq = ticketOrderSeq;
        this.gradeName = ticket.getGradeName();
        this.concertSeq = ticket.getConcertSeq();
    }

    @Override
    public String toString() {
        return "TicketOrderLog{" +
                "ticketSeq=" + ticketSeq +
                ", memberSeq=" + memberSeq +
                ", ticketOrderSeq=" + ticketOrderSeq +
                ", concertSeq=" + concertSeq +
                ", gradeName='" + gradeName + '\'' +
                '}';
    }
}
