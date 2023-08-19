package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class Ticket extends BaseEntity {

    private Long ticketId;
    private final String ticketSerialNumber;
    private final Long concertId;
    private final Long ticketGradeId;
    private OrderInfo orderInfo;

    @AutomapConstructor
    public Ticket(
            @Param("ticketId") Long ticketId,
            @Param("ticketSerialNumber") String ticketSerialNumber,
            @Param("concertId") Long concertId,
            @Param("ticketGradeId") Long ticketGradeId) {
        this.ticketId = ticketId;
        this.ticketSerialNumber = ticketSerialNumber;
        this.concertId = concertId;
        this.ticketGradeId = ticketGradeId;
    }

    public Ticket(long concertId, long ticketGradeId) {
        this.concertId = concertId;
        this.ticketSerialNumber = UUID.randomUUID().toString();
        this.ticketGradeId = ticketGradeId;
    }


    public static Ticket publish(TicketGrade ticketGrade){
        return new Ticket(ticketGrade.getConcertId(), ticketGrade.getTicketGradeId());
    }


    public void toOrderedTicket(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", ticketSerialNumber='" + ticketSerialNumber + '\'' +
                ", concertId=" + concertId +
                ", ticketGradeId=" + ticketGradeId +
                ", orderInfo=" + orderInfo +
                '}';
    }
}
