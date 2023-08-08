package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.TicketGrade;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface TicketMapper {

    void insertTicketGrade(List<TicketGrade> ticketGrades);

    List<TicketGrade> findTicketGrades(long concertId);

    void insertTicket(List<Ticket> tickets);

    List<Ticket> findTickets(long concertId);

}
