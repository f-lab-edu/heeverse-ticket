package com.heeverse.ticket.service;


import com.heeverse.common.factory.ConcertFactory;
import com.heeverse.common.factory.TicketFactory;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import org.junit.jupiter.api.Assertions;
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
class TicketIntegrationTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    protected ConcertFactory concertFactory;
    @Autowired
    protected TicketFactory ticketFactory;


    @Test
    @DisplayName("[성공] 요청한 티켓 등급 수는 티켓 등록 후 db에 저장된 티켓의 총개수와 동일하다.")
    void registerTicketSuccess() throws Exception {

        Long concertSeq = createConcert();

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
        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(createConcert(), LocalDateTime.now());

        // when
        ticketService.registerTicket(ticketRequestDto);

        // then
        assertThatThrownBy(() ->
                ticketService.registerTicket(ticketRequestDto)
        ).isInstanceOf(DuplicatedTicketException.class)
                .hasMessage("이미 등록된 티켓이 존재합니다");
    }

    private Long createConcert() {
        concertFactory.registerConcert(ConcertHelper.buildConcert());
        return concertFactory.selectLatestConcertSeq();
    }

}