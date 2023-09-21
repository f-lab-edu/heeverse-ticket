package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.persistence.TicketRemainsResponseMapperDto;

/**
 * @author gutenlee
 * @since 2023/09/21
 */

public class TicketRemainsResponseDto {

    public final long concertSeq;
    public final String gradeTicket;
    public final int remain;

    public TicketRemainsResponseDto(TicketRemainsResponseMapperDto mapperDto) {
        this.concertSeq = mapperDto.concertSeq();
        this.gradeTicket = mapperDto.gradeName();
        this.remain = mapperDto.remains();
    }

}
