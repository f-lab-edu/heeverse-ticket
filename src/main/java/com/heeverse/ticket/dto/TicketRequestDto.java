package com.heeverse.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;


public record TicketRequestDto (
        @JsonProperty("concertSeq")
        @NotNull
        Long concertSeq,
        @JsonProperty("ticketCategoryDtoList")
        @NotNull
        List<@Valid TicketGradeDto> ticketGradeDtoList
) {
}
