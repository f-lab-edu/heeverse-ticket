package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.service.TicketService;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gutenlee
 * @since 2023/08/07
 */
public class TicketTestHelper {
    public static final String VIP_GRADE = "VIP";
    public static final String S_GRADE = "S";
    public static final String R_GRADE = "R";

    public static final int  VIP_SEAT_COUNT = 1500;
    public static final int  S_SEAT_COUNT = 2000;
    public static final int  R_SEAT_COUNT = 4000;

    /**
     * dto
     */
    public static List<TicketGradeDto> createTicketCategoryDtos() {
        return List.of(
                new TicketGradeDto( "VIP", 1500),
                new TicketGradeDto( "S석", 2000),
                new TicketGradeDto( "R석", 4000)
        );
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDateTime concertDate) {
        return new TicketRequestDto(concertId, concertDate, createTicketCategoryDtos());
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDateTime concertDate, List<TicketGradeDto> list) {
        return new TicketRequestDto(concertId, concertDate, list);
    }

    /***
     * entity
     */
    public static List<GradeTicket> buildTicketGrade(Long concertSeq) {
        GradeTicket vipGrade = new GradeTicket(VIP_GRADE, VIP_SEAT_COUNT, concertSeq);
        GradeTicket sGrade = new GradeTicket(S_GRADE, S_SEAT_COUNT, concertSeq);
        GradeTicket rGrade = new GradeTicket(R_GRADE, R_SEAT_COUNT, concertSeq);
        return List.of(vipGrade, sGrade, rGrade);
    }


    public static Ticket buildTicket(Long concertSeq, GradeTicket gradeTicket) {
        return new Ticket(TicketSerialNumberHelper.createTicketSerialNumber(concertSeq, gradeTicket), gradeTicket);
    }
}
