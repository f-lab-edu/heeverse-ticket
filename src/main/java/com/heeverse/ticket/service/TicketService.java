package com.heeverse.ticket.service;

import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.dto.TicketRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Service
public class TicketService {

    private final TicketMapper ticketMapper;

    public TicketService(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public void registerTicket(TicketRequestDto ticketRequestDto) {

    }

}