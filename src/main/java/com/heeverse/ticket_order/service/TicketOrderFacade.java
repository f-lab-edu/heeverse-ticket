package com.heeverse.ticket_order.service;

import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import com.heeverse.ticket_order.service.event.TicketOrderEvent;
import com.heeverse.ticket_order.service.event.TicketOrderEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/13
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TicketOrderFacade {

    private final TicketService ticketService;
    private final TicketOrderService ticketOrderService;
    private final TicketOrderEventHandler ticketOrderEventHandler;

    public List<TicketOrderResponseDto> startTicketOrderJob(TicketOrderRequestDto dto, Long memberSeq) {
        try {
            Long ticketOrderSeq = orderTicket(dto, memberSeq);
            return ticketOrderService.getOrderTicket(ticketOrderSeq);
        } catch (Exception e) {
            log.error("티켓 예매가 실패했습니다. : {} ", e.getMessage());
            throw new TicketingFailException(e);
        }
    }

    protected Long orderTicket(TicketOrderRequestDto dto, Long memberSeq) throws Exception {
        Long ticketOrderSeq = ticketOrderService.createTicketOrder(memberSeq);
        ticketOrderEventHandler.saveTicketOrderLog(new TicketOrderEvent(dto, memberSeq, ticketOrderSeq));
        Assert.notNull(ticketOrderSeq, "ticketOrderSeq Must Not Null");
        ticketOrderService.orderTicket(dto, ticketOrderSeq);
        return ticketOrderSeq;
    }

    public List<TicketRemainsResponseDto> getTicketRemains(TicketRemainsDto ticketRemainsDto) {
        try {
            return ticketService.getTicketRemains(ticketRemainsDto.concertSeq());
        } catch (IllegalArgumentException e) {
            throw new TicketAggregationFailException(e);
        }
    }
}
