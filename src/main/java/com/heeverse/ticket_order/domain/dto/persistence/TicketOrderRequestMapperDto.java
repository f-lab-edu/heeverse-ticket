package com.heeverse.ticket_order.domain.dto.persistence;

import com.heeverse.ticket.domain.enums.BookingStatus;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/09/05
 */

public record TicketOrderRequestMapperDto(String concertName, LocalDateTime concertDate, String ticketSerialNumber,
                                          String gradeName, LocalDateTime bookingDate, BookingStatus bookingStatus) {

}
