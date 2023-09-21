package com.heeverse.ticket_order.service;

import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderUpdateMapperDto;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
    private Long ticketOrderSeq = null;
    private BookingStatus bookingStatus = BookingStatus.SUCCESS;

    public List<TicketOrderResponseDto> startTicketOrderJob(TicketOrderRequestDto dto, Long memberSeq) throws Exception{
        try {
            ticketOrderSeq = ticketOrderService.createTicketOrder(memberSeq);
            ticketService.getTicketLock(dto.ticketSetList());
            Assert.notNull(ticketOrderSeq);
            ticketOrderService.orderTicket(dto, ticketOrderSeq);

        } catch (Exception e) {
            log.error("티켓 예매가 실패했습니다. : {} ", e.getMessage());
            bookingStatus = BookingStatus.FAIL;
            throw new TicketingFailException(e);
        }
        ticketOrderService.changeTicketOrderStatus(new TicketOrderUpdateMapperDto(ticketOrderSeq, bookingStatus));
        return ticketOrderService.getOrderTicket(ticketOrderSeq);
    }


    public List<TicketRemainsResponseDto> getTicketRemains(TicketRemainsDto ticketRemainsDto) {

        try {
            return ticketService.getTicketRemains(ticketRemainsDto.concertSeq());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
