package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.common.SerialNumber;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class Ticket extends BaseEntity {

    private Long seq;
    private final String ticketSerialNumber;
    private final Long concertSeq;
    private final String gradeName;
    private OrderInfo orderInfo;

    @AutomapConstructor
    private Ticket(
            @Param("seq") Long seq,
            @Param("ticketSerialNumber") String ticketSerialNumber,
            @Param("concertSeq") Long concertSeq,
            @Param("gradeName") String gradeName
    ) {
        this.seq = seq;
        this.ticketSerialNumber = ticketSerialNumber;
        this.concertSeq = concertSeq;
        this.gradeName = gradeName;
    }


    public Ticket(SerialNumber<String> ticketSerialNumber, GradeTicket gradeTicket) {
        this.ticketSerialNumber = ticketSerialNumber.getSerial();
        this.concertSeq = gradeTicket.getConcertSeq();
        this.gradeName = gradeTicket.getGradeName();
    }


    public void toOrderedTicket(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }




    @Override
    public String toString() {
        return "Ticket{" +
                "seq=" + seq +
                ", ticketSerialNumber='" + ticketSerialNumber + '\'' +
                ", concertSeq=" + concertSeq +
                ", orderInfo=" + orderInfo +
                '}';
    }
}
