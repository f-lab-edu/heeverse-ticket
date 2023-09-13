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
    private String concertName;
    private LocalDateTime concertDate;
    private String ticketSerialNumber;
    private String gradeName;
    private LocalDateTime bookingDate;
    private String bookingStatus;

    public TicketOrderResponseDto(TicketOrderRequestMapperDto dto) {
        this.concertName = dto.concertName();
        this.concertDate = dto.concertDate();
        this.ticketSerialNumber = dto.ticketSerialNumber();
        this.gradeName = dto.gradeName();
        this.bookingDate = dto.bookingDate();
        this.bookingStatus = dto.bookingStatus().getStatus();
    }
}
