package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.common.IntegrationTestService;
import com.heeverse.common.factory.ConcertFactory;
import com.heeverse.common.factory.TicketFactory;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author jeongheekim
 * @date 10/3/23
 */
@Sql(scripts = "/test-data/create-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/delete-test-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TicketOrderConcurrencyTest extends IntegrationTestService {

    @Autowired
    protected ConcertFactory concertFactory;
    @Autowired
    protected TicketFactory ticketFactory;
    @Autowired
    private TicketOrderFacade ticketOrderFacade;
    @Autowired
    private TicketOrderMapper ticketOrderMapper;
    private static final int threadCount = 2;
    private static final int threadPoolSize = 32;
    private final Logger log = (Logger) LoggerFactory.getLogger(TicketOrderFacadeTest.class);

    @DisplayName("동일한 티켓 예매 시 ticketOrder는 threadCount만큼 Insert된다.")
    @Test
    void successBookingStatusTest2() throws InterruptedException {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());
        concurrencyRequest(ticketList, memberSeq);
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectTicketOrderListByMemberSeq(memberSeq);
        Assertions.assertEquals(threadCount, ticketOrderList.size());
    }

    @DisplayName("동일한 티켓 예매 시 주문상태가 SUCCESS인 주문은 1건만 존재한다.")
    @Test
    void concurrencyBookingSuccessTest() throws InterruptedException {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());
        concurrencyRequest(ticketList, memberSeq);
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectTicketOrderListByMemberSeq(memberSeq);
        long expectedCount = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.SUCCESS))
                .count();
        Assertions.assertEquals(expectedCount, 1);
    }

    @DisplayName("동일한 티켓 예매 시 주문상태가 FAIL인 주문은 threadCount -1 건 존재한다.")
    @Test
    void concurrencyBookingFailTest() throws InterruptedException {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());
        concurrencyRequest(ticketList, memberSeq);
        List<TicketOrder> ticketOrderList = ticketOrderMapper.selectTicketOrderListByMemberSeq(memberSeq);
        long expectedCount = ticketOrderList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.FAIL))
                .count();
        Assertions.assertEquals(expectedCount, threadCount - 1);
    }

    private void concurrencyRequest(List<Long> ticketSeqList, Long memberSeq) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderTicket(ticketSeqList, memberSeq);
                } catch (Exception e) {
                    log.error("[TicketOrderFacadeTest] ticket order test fail : {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }


    private Long createConcert() {
        return concertFactory.selectLatestConcertSeq();
    }

    private List<Long> createTicket(Long concertSeq) {
        List<Ticket> tickets = ticketFactory.selectTicketSeqList(concertSeq);
        return tickets.stream().map(Ticket::getSeq).collect(Collectors.toList());
    }

    private void orderTicket(List<Long> ticketList, Long memberSeq) {
        ticketOrderFacade.startTicketOrderJob(TicketOrderTestHelper.createTicketOrderRequestDto(ticketList), memberSeq);
    }
}
