package com.heeverse.ticket.service;


import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("local")
@SpringBootTest
class TicketServiceTest {

    @Autowired
    TicketService ticketService;


    @Test
    @DisplayName("[성공] 티켓 등록한다")
    void register_ticket_success() throws Exception {

        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(1L, LocalDate.now());

        ticketService.registerTicket(ticketRequestDto);

        int expectedTicketCount = ticketRequestDto.ticketGradeDtoList().stream()
                .mapToInt(TicketGradeDto::seatCount)
                .sum();

        Assertions.assertEquals(ticketService.getTicket(1L).size(), expectedTicketCount);
    }


    @Test
    @DisplayName("[실패] 이미 등록된 티켓이 존재하면 티켓 등록은 실패한다")
    void register_ticket_fail() throws Exception {

        // given
        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(1L, LocalDate.now());

        // when
        register_ticket_success();

        // then
        assertThatThrownBy(() ->
                ticketService.registerTicket(ticketRequestDto)
        ).isInstanceOf(DuplicatedTicketException.class)
                .hasMessage("이미 등록된 티켓이 존재합니다");
    }



}