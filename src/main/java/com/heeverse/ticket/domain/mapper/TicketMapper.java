package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.dto.persistence.TicketRequestMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketRemainsResponseMapperDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TicketMapper {

    void insertTicketGrade(List<GradeTicket> gradeTickets);

    List<GradeTicket> findTicketGrades(long concertSeq);

    void insertTicket(List<Ticket> tickets);

    List<Ticket> findTickets(long concertSeq);

    int countTicket(long concertSeq);

    List<Ticket> findTicketsByTicketSeqList(List<Long> ticketSeqList);

    int updateTicketOrderSeq(TicketRequestMapperDto dto);

    List<Ticket> getTicketLock(@Param("ticketSeqList") List<Long> ticketSeqList);

    int rollbackTicketOrderSeq(List<Long> ticketSeqList);
    List<TicketRemainsResponseMapperDto> aggregateTicketRemains(@Param("concertSeq") long concertSeq,
                                                                @Param("cancelledFlag") boolean cancelledFlag);
}
