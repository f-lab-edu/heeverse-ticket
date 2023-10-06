package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jeongheekim
 * @date 9/30/23
 */
public class TicketSerialNumberHelper {
    public static TicketSerialNumber createTicketSerialNumber(Long concertSeq, GradeTicket gradeTicket){
        return new TicketSerialNumber(new TicketSerialTokenDto(LocalDateTime.now(), concertSeq, gradeTicket, 1));
    }
}
