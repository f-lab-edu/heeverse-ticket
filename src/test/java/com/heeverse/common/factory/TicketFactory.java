package com.heeverse.common.factory;

import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/27
 */
@Component
public class TicketFactory {

    @Autowired
    private TicketMapper ticketMapper;

    public void insertTicket(List<Ticket> tickets) {
        ticketMapper.insertTicket(tickets);
    }

    public void insertTicketGrade(List<GradeTicket> gradeTickets) {
        ticketMapper.insertTicketGrade(gradeTickets);
    }

    public List<Ticket> selectTicketSeqList(Long concertSeq) {
        return ticketMapper.findTickets(concertSeq);
    }
}


