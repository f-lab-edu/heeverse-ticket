package com.heeverse.ticket.service;


import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.dto.TicketRequestDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.IllegalTransactionStateException;

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
    void parentTransactionNotExistExceptionTest() {

        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq,
            LocalDateTime.now());

        Assertions.assertThrows(IllegalTransactionStateException.class,
            () -> ticketService.registerTicket(ticketRequestDto));
    }
}