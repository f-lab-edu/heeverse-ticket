package com.heeverse.ticket.domain;

import com.heeverse.common.Delimiter;
import com.heeverse.common.SerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/08/11
 */
@Getter
public class TicketSerialTokenDto extends SerialTokenDto {

    private final static Delimiter defaultDelimiter = Delimiter.DASH;

    private final LocalDateTime concertDate;
    private final long concertSeq;
    private final String gradeName;
    private final int index;
    private final int ticketCount;


    public TicketSerialTokenDto(@NonNull LocalDateTime concertDate, long concertSeq, @NonNull GradeTicket grade, int idx) {
        super(defaultDelimiter);
        this.concertDate = concertDate;
        this.concertSeq = concertSeq;
        this.gradeName = grade.getGradeName();
        this.index = idx;
        this.ticketCount = grade.getTicketCount();
    }

    public int getTicketCountStrLength(){
        return Integer.toString(ticketCount).length();
    }

    public String getIndexAsString() {
        return Integer.toString(index);
    }

    public String getConcertSeqAsLong() {
        return Long.toString(concertSeq);
    }
}
