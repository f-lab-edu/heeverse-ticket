package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class Ticket extends BaseEntity {

    private Long seq;
    private final String ticketSerialNumber;
    private final Long concertSeq;
    private OrderInfo orderInfo;

    @AutomapConstructor
    public Ticket(
            @Param("seq") Long seq,
            @Param("ticketSerialNumber") String ticketSerialNumber,
            @Param("concertSeq") Long concertSeq) {
        this.seq = seq;
        this.ticketSerialNumber = ticketSerialNumber;
        this.concertSeq = concertSeq;
    }

    public Ticket(long concertSeq) {
        this.concertSeq = concertSeq;
        this.ticketSerialNumber = UUID.randomUUID().toString();
    }


    public Ticket (TicketGrade ticketGrade){
        this(ticketGrade.getConcertId());
    }


    public void toOrderedTicket(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "seq=" + seq +
                ", ticketSerialNumber='" + ticketSerialNumber + '\'' +
                ", concertId=" + concertSeq +
                ", orderInfo=" + orderInfo +
                '}';
    }
}
