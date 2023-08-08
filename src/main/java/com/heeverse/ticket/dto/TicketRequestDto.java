package com.heeverse.ticket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * @author jeongheekim
 * @date 2023/08/08
 */
@AllArgsConstructor
public record TicketRequestDto (
    //@JsonProperty("concertId")
    @NotNull
    Long concertId,
    //@JsonProperty("ticketCategoryDtoList")
    @NotNull
    List<@Valid TicketGradeDto> ticketGradeDtoList
) {
}
