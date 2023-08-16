package com.heeverse.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;


public record TicketRequestDto (
        @JsonProperty("concertSeq")
        @NotNull
        Long concertSeq,

        LocalDate concertDate,

        @JsonProperty("ticketCategoryDtoList")
        @NotNull
        List<@Valid TicketGradeDto> ticketGradeDtoList
) {
}
