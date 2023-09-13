package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/13
 */
public class TicketOrderTestHelper {
    public static TicketOrderRequestDto createTicketOrderRequestDto(List<Long> ticketSeqList) {
        return new TicketOrderRequestDto(ticketSeqList);
    }




}
