package com.heeverse.ticket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/08
 */
public record TicketRequestDto (
    //@JsonProperty("concertId")
    @NotNull
    Long concertId,
    //@JsonProperty("ticketCategoryDtoList")
    @NotNull
    List<@Valid GradeTicketDto> gradeTicketDtoList
) {
}
