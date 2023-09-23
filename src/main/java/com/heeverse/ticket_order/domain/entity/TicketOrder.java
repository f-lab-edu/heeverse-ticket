package com.heeverse.ticket_order.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.domain.enums.BookingStatus;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
@Getter
public class TicketOrder extends BaseEntity {
    private final Long seq;
    private final Long memberSeq;
    private final LocalDateTime bookingDate;
    private final BookingStatus bookingStatus;
    private final boolean deleteFlag;
    @AutomapConstructor
    private TicketOrder(@Param("seq")Long seq,
                        @Param("memberSeq")Long memberSeq,
                        @Param("bookingDate")LocalDateTime bookingDate,
                        @Param("bookingStatus")BookingStatus bookingStatus,
                        @Param("deleteFlag")boolean deleteFlag){
        this.seq = seq;
        this.memberSeq = memberSeq;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        this.deleteFlag = deleteFlag;
    }



 /*   public TicketOrder(Long seq, Long memberSeq, LocalDateTime bookingDate, BookingStatus bookingStatus, boolean deleteFlag) {
        System.out.println("!!!!!!!!!!!!bookingStatus = " + bookingStatus);
        this.seq = seq;
        this.memberSeq = memberSeq;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        this.deleteFlag = deleteFlag;
    }*/

    public TicketOrder(Long memberSeq, LocalDateTime bookingDate, BookingStatus bookingStatus) {
        this(null, memberSeq, bookingDate, bookingStatus, false);
    }

    @Override
    public String toString() {
        return "TicketOrder{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", bookingDate=" + bookingDate +
                ", bookingStatus=" + bookingStatus +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}

