package com.heeverse.ticket.service;


import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class TicketIntegrationTestService {
    private static Long concertSeq;

    @Autowired
    TicketService ticketService;


    @BeforeAll
    static void setUp() {
        concertSeq = TicketTestHelper.콘서트_생성하고_시퀀스_반환();
    }

    @Test
    @DisplayName("[성공] 요청한 티켓 등급 수는 티켓 등록 후 db에 저장된 티켓의 총개수와 동일하다.")
    void registerTicketSuccess() throws Exception {

        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq, LocalDateTime.now());

        ticketService.registerTicket(ticketRequestDto);

        int expectedTicketCount = ticketRequestDto.ticketGradeDtoList().stream()
            .mapToInt(TicketGradeDto::seatCount)
            .sum();

        Assertions.assertEquals(ticketService.getTicket(concertSeq).size(), expectedTicketCount);
    }


    @Test
    @DisplayName("[실패] 이미 등록된 티켓이 존재하면 티켓 등록은 실패한다")
    void registerTicketFail() throws Exception {

        // given
        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq, LocalDateTime.now());

        // when
        registerTicketSuccess();

        // then
        assertThatThrownBy(() ->
            ticketService.registerTicket(ticketRequestDto)
        ).isInstanceOf(DuplicatedTicketException.class)
            .hasMessage("이미 등록된 티켓이 존재합니다");
    }

}