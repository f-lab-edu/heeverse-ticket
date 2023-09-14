package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
@Getter
public class TicketOrderResponseDto {
    private final String concertName;
    private final LocalDateTime concertDate;
    private final String ticketSerialNumber;
    private final String gradeName;
    private final LocalDateTime bookingDate;
    private final String bookingStatus;

    public TicketOrderResponseDto(TicketOrderRequestMapperDto dto) {
        this.concertName = dto.concertName();
        this.concertDate = dto.concertDate();
        this.ticketSerialNumber = dto.ticketSerialNumber();
        this.gradeName = dto.gradeName();
        this.bookingDate = dto.bookingDate();
        this.bookingStatus = dto.bookingStatus().getDescription();
    }
}
