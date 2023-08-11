package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.GradeTicket;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface TicketMapper {

    void insertTicketGrade(List<GradeTicket> gradeTickets);

    List<GradeTicket> findTicketGrades(long concertId);

    void insertTicket(List<Ticket> tickets);

    List<Ticket> findTickets(long concertId);

    int countTicket(long concertId);

}
