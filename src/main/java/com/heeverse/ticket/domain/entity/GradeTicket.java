package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.dto.TicketGradeDto;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class GradeTicket extends BaseEntity {

    private Long seq;
    private final String gradeName;
    private final Integer ticketCount;
    private final Long concertId;

    @AutomapConstructor
    private GradeTicket(
            @Param("seq") Long seq,
            @Param("gradeName") String gradeName,
            @Param("ticketCount") Integer ticketCount,
            @Param("concertSeq") Long concertSeq) {
        this.seq = seq;
        this.gradeName = gradeName;
        this.ticketCount = ticketCount;
        this.concertId = concertSeq;
    }


    public GradeTicket(TicketGradeDto ticketGradeDto, long concertId) {
        this(null,
                ticketGradeDto.gradeName(),
                ticketGradeDto.seatCount(),
                concertId);
    }

}
