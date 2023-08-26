package com.heeverse.ticket.service;


import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("local")
@SpringBootTest
class TicketServiceTest {
    private static Long concertSeq;

    @Autowired
    TicketService ticketService;


    @BeforeAll
    static void setUp() {
        concertSeq = TicketTestHelper.콘서트_생성하고_시퀀스_반환();
    }

    @Test
    @DisplayName("[성공] 티켓 등록한다")
    void register_ticket_success() throws Exception {

        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq, LocalDateTime.now());

        ticketService.registerTicket(ticketRequestDto);

        int expectedTicketCount = ticketRequestDto.ticketGradeDtoList().stream()
                .mapToInt(TicketGradeDto::seatCount)
                .sum();

        Assertions.assertEquals(ticketService.getTicket(concertSeq).size(), expectedTicketCount);
    }


    @Test
    @DisplayName("[실패] 이미 등록된 티켓이 존재하면 티켓 등록은 실패한다")
    void register_ticket_fail() throws Exception {

        // given
        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq, LocalDateTime.now());

        // when
        register_ticket_success();

        // then
        assertThatThrownBy(() ->
                ticketService.registerTicket(ticketRequestDto)
        ).isInstanceOf(DuplicatedTicketException.class)
                .hasMessage("이미 등록된 티켓이 존재합니다");
    }



}