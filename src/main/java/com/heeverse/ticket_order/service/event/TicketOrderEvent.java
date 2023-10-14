package com.heeverse.ticket_order.service.event;

import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import lombok.Getter;

/**
 * @author gutenlee
 * @since 2023/10/14
 */
public record TicketOrderEvent(
        TicketOrderRequestDto dto,
        long memberSeq,
        long ticketOrderSeq
) {

}
