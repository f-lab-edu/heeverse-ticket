package com.heeverse.ticket.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TicketGradeDto(
        @NotBlank
        String gradeName,
        @Min(1)
        int seatCount
) {

}
