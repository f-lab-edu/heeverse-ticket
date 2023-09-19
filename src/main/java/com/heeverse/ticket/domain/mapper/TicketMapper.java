package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.dto.persistence.TicketRequestMapperDto;
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

    List<Long> selectForUpdate(List<Long> ticketSetList);

    int updateTicketOrderSeq(TicketRequestMapperDto dto);

    List<Ticket> getTicketLock(@Param("ticketSeqList") List<Long> ticketSeqList);

}
