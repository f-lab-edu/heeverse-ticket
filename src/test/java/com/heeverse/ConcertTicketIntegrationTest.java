package com.heeverse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket.service.TicketService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/08/26
 */
@ActiveProfiles("local")
@Transactional
@SpringBootTest
public class ConcertTicketIntegrationTest {

    private static Long concertSeq;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private TicketService ticketService;

    private static List<ConcertRequestDto> concertRequestDtoList;

    private static List<TicketGradeDto> ticketGradeList;

    @BeforeAll
    static void setUp() {
        concertSeq = TicketTestHelper.콘서트_생성하고_시퀀스_반환();
        LocalDateTime concertDate = LocalDateTime.parse("2023-12-15T10:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-10-15T10:00:00");
        LocalDateTime ticketEndTime = LocalDateTime.parse("2023-10-17T10:00:00");

        ticketGradeList = TicketTestHelper.createTicketCategoryDtos();
        ConcertRequestDto dto = new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, ticketGradeList);
        concertRequestDtoList = List.of(dto);
    }

    @Test
    @DisplayName("[성공] 콘서트 등록 후 티켓 등록한다")
    void registerConcertAndThenTicketSuccess() {

        concertService.registerConcert(concertRequestDtoList);

        int expectedTicketCount = ticketGradeList.stream()
            .mapToInt(TicketGradeDto::seatCount)
            .sum();

        Assertions.assertEquals(ticketService.getTicket(concertSeq).size(), expectedTicketCount);
    }


    @Test
    @DisplayName("[실패] 이미 등록된 티켓이 존재하면 티켓 등록은 실패한다")
    void registerTicketFail() {

        // when
        concertService.registerConcert(concertRequestDtoList);

        // then
        assertThatThrownBy(() ->
            concertService.registerConcert(concertRequestDtoList)
        ).isInstanceOf(DuplicatedTicketException.class)
            .hasMessage("이미 등록된 티켓이 존재합니다");
    }
}
