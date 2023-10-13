package com.heeverse.ticket_order.service;

import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public List<TicketOrderResponseDto> startTicketOrderJob(TicketOrderRequestDto dto, Long memberSeq) {
        try {
            Long ticketOrderSeq = orderTicket(dto, memberSeq);
            saveOrderLog(dto, memberSeq, ticketOrderSeq);
            return ticketOrderService.getOrderTicket(ticketOrderSeq);
        } catch (Exception e) {
            log.error("티켓 예매가 실패했습니다. : {} ", e.getMessage());
            throw new TicketingFailException(e);
        }
    }

    protected Long orderTicket(TicketOrderRequestDto dto, Long memberSeq) throws Exception {
        Long ticketOrderSeq = ticketOrderService.createTicketOrder(memberSeq);
        ticketService.getTicketLock(dto.ticketSetList());
        Assert.notNull(ticketOrderSeq);
        ticketOrderService.orderTicket(dto, ticketOrderSeq);
        return ticketOrderSeq;
    }


    public List<TicketRemainsResponseDto> getTicketRemains(TicketRemainsDto ticketRemainsDto) {
        try {
            return ticketService.getTicketRemains(ticketRemainsDto.concertSeq());
        } catch (IllegalArgumentException e) {
            throw  new TicketAggregationFailException(e);
        }
    }

    private void saveOrderLog(TicketOrderRequestDto dto, Long memberSeq, Long ticketOrderSeq) {
        ticketOrderService.saveTicketOrderLog(dto, memberSeq, ticketOrderSeq);
    }
}
