package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.dto.TicketGradeDto;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.type.Alias;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
@Alias("ticketGrade")
public class TicketGrade extends BaseEntity {

    private Long seq;
    private final String gradeName;
    private final Integer ticketCount;
    private final Long concertId;

    @AutomapConstructor
    private TicketGrade(
            @Param("seq") Long seq,
            @Param("gradeName") String gradeName,
            @Param("ticketCount") Integer ticketCount,
            @Param("concertSeq") Long concertSeq) {
        this.seq = seq;
        this.gradeName = gradeName;
        this.ticketCount = ticketCount;
        this.concertId = concertSeq;
    }


    public TicketGrade(TicketGradeDto ticketGradeDto, long concertId) {
        this(null,
                ticketGradeDto.gradeName(),
                ticketGradeDto.seatCount(),
                concertId);
    }

}
