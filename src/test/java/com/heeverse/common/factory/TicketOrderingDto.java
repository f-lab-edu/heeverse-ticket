package com.heeverse.common.factory;

import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gutenlee
 * @since 2023/10/15
 */
public class TicketOrderingDto {

    private final long memberSeq;
    private final long concertSeq;
    private final List<GradeTicket> gradeTicketList;
    private final List<Ticket> ticketList;

    public TicketOrderingDto(long memberSeq, long concertSeq, List<GradeTicket> gradeTicketList, List<Ticket> ticketList) {
        this.memberSeq = memberSeq;
        this.concertSeq = concertSeq;
        this.gradeTicketList = gradeTicketList;
        this.ticketList = ticketList;
    }

    public List<Long> getCreatedTicketSeqList() {
        return ticketList.stream()
                .map(Ticket::getSeq)
                .collect(Collectors.toList());
    }

    public long getMemberSeq() {
        return memberSeq;
    }

    public long getConcertSeq() {
        return concertSeq;
    }

    public List<GradeTicket> getGradeTicketList() {
        return gradeTicketList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }
}