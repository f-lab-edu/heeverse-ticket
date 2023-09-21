package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
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
    private TicketService ticketService;
    private static final int threadCount = 4;
    private static final int threadPoolSize = 32;
    private AtomicInteger failCount;
    private AtomicInteger successCount;

    @BeforeEach
    void setUp() throws InterruptedException {
        Logger log = (Logger) LoggerFactory.getLogger(TicketOrderFacadeTest.class);
        List<Long> ticketSeqList = TicketTestHelper.createTicketSeq();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(threadCount);
        TicketOrderRequestDto dto = createTicketOrderRequestDto(ticketSeqList);
        successCount = new AtomicInteger(0);
        failCount = new AtomicInteger(0);
        Long memberSeq = 14L;
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketOrderFacade.orderTicket(dto, memberSeq);
                    successCount.incrementAndGet();
                    log.info("예매 성공");
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.error("[TicketOrderFacadeTest] ticket order test fail : {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    @DisplayName("요청한 ticket 수와 예매된 티켓수가 일치한다.")
    @Test
    void ticketOrderInsertSizeTest() throws Exception {
        List<Long> ticketSeqList = TicketTestHelper.createTicketSeq();
        long orderCount = ticketService.getTicketsByTicketSeqList(ticketSeqList)
                .stream()
                .filter(t -> Objects.nonNull(t.getOrderSeq())).count();
        Assertions.assertEquals(orderCount, ticketSeqList.size());
    }

    @DisplayName("succesCount와 failCount의 합은 threadCount와 같다.")
    @Test
    void ticketOrderFailCountTest() {
        Assertions.assertEquals(threadCount, failCount.get() + successCount.get());
    }

}