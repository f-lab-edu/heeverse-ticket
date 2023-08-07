package com.heeverse.ticket.dto;


import jakarta.validation.constraints.NotNull;

public record TicketGradeDto(
        @NotNull
        Integer ticketGrade,
        @NotNull
        String gradeName,
        @NotNull
        Integer seatCount
) {

}
