package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.dto.TicketGradeDto;
import lombok.Getter;
import org.apache.ibatis.annotations.AutomapConstructor;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Getter
public class TicketGrade extends BaseEntity {

    private Long gradeId;
    private final Integer grade;
    private final String gradeName;
    private final Integer seatCount;
    private final Long concertId;

    private TicketGrade(Integer grade, String gradeName, Integer seatCount, long concertId) {
        this.grade = grade;
        this.gradeName = gradeName;
        this.seatCount = seatCount;
        this.concertId = concertId;
    }


    @AutomapConstructor
    public TicketGrade(Long gradeId, Integer grade, String gradeName, Integer seatCount, Long concertId) {
        this.gradeId = gradeId;
        this.grade = grade;
        this.gradeName = gradeName;
        this.seatCount = seatCount;
        this.concertId = concertId;
    }



    public static TicketGrade toEntity(TicketGradeDto ticketGradeDto, long concertId) {
        return new TicketGrade(
                ticketGradeDto.ticketGrade(),
                ticketGradeDto.gradeName(),
                ticketGradeDto.seatCount(),
                concertId);
    }

}
