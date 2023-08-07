package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import lombok.Getter;

import java.util.UUID;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class Ticket extends BaseEntity {

    private Long ticketId;
    private final String ticketSerialNumber;
    private final Long concertId;
    private final Long gradeId;
    private OrderInfo orderInfo;

    private Ticket(long concertId, long gradeId) {
        this.concertId = concertId;
        this.ticketSerialNumber = UUID.randomUUID().toString();
        this.gradeId = gradeId;
    }


    private Ticket(Ticket ticket, OrderInfo orderInfo) {
        this.ticketId = ticket.getTicketId();
        this.ticketSerialNumber = ticket.getTicketSerialNumber();
        this.concertId = ticket.getConcertId();
        this.gradeId = ticket.getGradeId();
        this.orderInfo = orderInfo;
    }


    public static Ticket publish(TicketGrade ticketGrade){
        return new Ticket(ticketGrade.getConcertId(), ticketGrade.getGradeId());
    }


    public static Ticket toOrderedTicket(Ticket ticket, OrderInfo orderInfo) {
        return new Ticket(ticket, orderInfo);
    }

}
