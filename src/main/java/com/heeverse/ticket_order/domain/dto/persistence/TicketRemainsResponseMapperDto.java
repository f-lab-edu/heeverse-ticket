package com.heeverse.ticket_order.domain.dto.persistence;

public record TicketRemainsResponseMapperDto(
        long concertSeq,
        String gradeName,
        int remains
) {
}
