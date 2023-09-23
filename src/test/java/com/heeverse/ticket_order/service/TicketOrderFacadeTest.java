package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.heeverse.ticket_order.service.TicketOrderTestHelper.createTicketOrderRequestDto;

/**
 * @author jeongheekim
 * @date 2023/09/10
 */
@ActiveProfiles("dev")
@Transactional
@SpringBootTest
class TicketOrderFacadeTest {

    @Autowired
    private TicketOrderFacade ticketOrderFacade;
    @Autowired
    private TicketOrderMapper ticketOrderMapper;
    private static final int threadCount = 4;
    private static final int threadPoolSize = 32;
    private final Logger log = (Logger) LoggerFactory.getLogger(TicketOrderFacadeTest.class);


    @DisplayName("동일한 티켓 예매 시 주문상태가 SUCESS인 주문은 1건만 존재한다.")
    @Test
    void successBookingStatusTest() throws InterruptedException {
        setUp();
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectAllTicketOrderList();
        long count = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.SUCCESS))
                .count();
        Assertions.assertEquals(count, 1);
    }

    void setUp() throws InterruptedException {
        List<Long> ticketSeqList = TicketTestHelper.createTicketSeq();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(threadCount);
        TicketOrderRequestDto dto = createTicketOrderRequestDto(ticketSeqList);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketOrderFacade.orderTicket(dto, MemberTestHelper.getMemberSeq());
                } catch (Exception e) {
                    log.error("[TicketOrderFacadeTest] ticket order test fail : {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}