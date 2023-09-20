package com.heeverse.ticket_order.domain.dto.persistence;

import com.heeverse.ticket.domain.enums.BookingStatus;

/**
 * @author jeongheekim
 * @date 2023/09/20
 */
public record TicketOrderUpdateMapperDto(Long ticketOrderSeq,BookingStatus bookingStatus) {
}
