package com.heeverse.ticket_order.domain.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
public record TicketOrderRequestDto(@Size(min = 1, max = 5) List<Long> ticketSetList) {

}
