package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Autowired
    private TicketService ticketService;
    private static final int threadCount = 4;
    private static final int threadPoolSize = 32;
    private final Logger log = (Logger) LoggerFactory.getLogger(TicketOrderFacadeTest.class);


    @DisplayName("이미 예매한 티켓이 없으면 티켓예매는 성공한다.")
    @Test
    void ticketOrderSuccessTest() throws Exception {
        int expectedSuccessCount = 1;
        orderTicket();
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectAllTicketOrderList();
        long ticketOrderSuccessCount = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.SUCCESS))
                .count();
        Assertions.assertEquals(expectedSuccessCount, ticketOrderSuccessCount);
    }

    @DisplayName("이미 예매한 티켓을 예매하면 실패한다.")
    @Test
    void ticketOrderFailTest() throws Exception {
        orderTicket();
        Assertions.assertThrows(TicketingFailException.class,
                () -> ticketOrderFacade.startTicketOrderJob(createTicketOrderRequestDto(TicketTestHelper.createTicketSeq()), MemberTestHelper.getMemberSeq()));
    }

    @Rollback
    @DisplayName("동일한 티켓 예매 시 ticketOrder는 threadCount만큼 Insert된다.")
    @Test
    void successBookingStatusTest2() throws InterruptedException {
        concurrencyRequest();
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectAllTicketOrderList();
        Assertions.assertEquals(ticketOrderList.size(), threadCount);
        rollback();
    }
    @Rollback
    @DisplayName("동일한 티켓 예매 시 주문상태가 SUCESS인 주문은 1건만 존재한다.")
    @Test
    void concurrencyBookingSuccessTest() throws InterruptedException {
        concurrencyRequest();
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectAllTicketOrderList();
        long expectedCount = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.SUCCESS))
                .count();
        Assertions.assertEquals(expectedCount, 1);
        rollback();
    }
    @Rollback
    @DisplayName("동일한 티켓 예매 시 주문상태가 FAIL인 주문은 threadCount -1 건 존재한다.")
    @Test
    void concurrencyBookingFailTest() throws InterruptedException {
        concurrencyRequest();
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectAllTicketOrderList();
        long expectedCount = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.FAIL))
                .count();
        Assertions.assertEquals(expectedCount, threadCount - 1);
        rollback();
    }

    private void concurrencyRequest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderTicket();
                } catch (Exception e) {
                    log.error("[TicketOrderFacadeTest] ticket order test fail : {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    private void orderTicket() throws Exception {
        TicketOrderRequestDto ticketOrderRequestDto = createTicketOrderRequestDto(TicketTestHelper.createTicketSeq());
        ticketOrderFacade.startTicketOrderJob(ticketOrderRequestDto, MemberTestHelper.getMemberSeq());
    }

    private void rollback() {
        ticketService.rollbackTicketOrderSeq(TicketTestHelper.createTicketSeq());
        ticketOrderMapper.rollback();
    }

}