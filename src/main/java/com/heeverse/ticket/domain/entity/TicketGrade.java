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

    private Long ticketGradeId;
    private final String gradeName;
    private final Integer seatCount;
    private final Long concertId;

    @AutomapConstructor
    private TicketGrade(
            @Param("ticketGradeId") Long ticketGradeId,
            @Param("gradeName") String gradeName,
            @Param("seatCount") Integer seatCount,
            @Param("concertId") Long concertId) {
        this.ticketGradeId = ticketGradeId;
        this.gradeName = gradeName;
        this.seatCount = seatCount;
        this.concertId = concertId;
    }


    public TicketGrade(TicketGradeDto ticketGradeDto, long concertId) {
        this(null,
                ticketGradeDto.gradeName(),
                ticketGradeDto.seatCount(),
                concertId);
    }

}
