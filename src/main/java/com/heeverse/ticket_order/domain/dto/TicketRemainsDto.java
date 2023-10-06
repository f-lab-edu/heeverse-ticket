package com.heeverse.ticket_order.domain.dto;

import jakarta.validation.constraints.NotNull;

/**
 * @author gutenlee
 * @since 2023/09/21
 */
public record TicketRemainsDto(
        @NotNull Long concertSeq
) {
}
