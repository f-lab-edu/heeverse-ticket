package com.heeverse.ticket.service;


import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("local")
@SpringBootTest
class TicketServiceTest {

    private static Long concertSeq;

    @Autowired
    private TicketService ticketService;

    @BeforeAll
    static void setUp() {
        concertSeq = TicketTestHelper.콘서트_생성하고_시퀀스_반환();
    }

    @Test
    @DisplayName("부모 트랜잭션이 없으면 IllegalTransactionStateException을 던진다.")
    void parent_transaction_not_exist_exception_test() throws Exception {

        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq,
            LocalDateTime.now());

        Assertions.assertThrows(IllegalTransactionStateException.class,
            () -> ticketService.registerTicket(ticketRequestDto));
    }
}