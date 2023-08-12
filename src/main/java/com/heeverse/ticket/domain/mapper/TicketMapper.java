package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.TicketGrade;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper {

    void insertTicketGrade(List<TicketGrade> ticketGrades);

    List<TicketGrade> findTicketGrades(long concertId);

    void insertTicket(List<Ticket> tickets);

    List<Ticket> findTickets(long concertId);

}
