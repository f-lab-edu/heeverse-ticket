package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.heeverse.ticket_order.service.TicketOrderTestHelper.createTicketOrderRequestDto;

/**
 * @author jeongheekim
 * @date 2023/09/10
 */
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class TicketOrderFacadeTest {

    @Autowired
    private TicketOrderFacade ticketOrderFacade;
    @Autowired
    private TicketService ticketService;
    private static Long ticketOrderSeq;
    private static List<Long> ticketSeqList;
    private final int threadCount = 10;
    private final int threadPoolSize = 32;
    private AtomicInteger failCount = new AtomicInteger(0);


    @Rollback
    @Nested
    @DisplayName("동시에 동일한 티켓 요청 시 ticket 테이블에")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class concurrentlySuccessTest {
        Logger log = (Logger) LoggerFactory.getLogger(TicketOrderFacadeTest.class);

        @BeforeAll
        static void setUpTicketSeqList() {
            ticketSeqList = TicketTestHelper.createTicketSeq();
        }

        @Test
        @DisplayName("ticketOrderSeq는 NotNull이다.")
        @Order(1)
        void setUp() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            CountDownLatch latch = new CountDownLatch(threadCount);
            TicketOrderRequestDto dto = createTicketOrderRequestDto(ticketSeqList);
            Long memberSeq = 14L;
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        ticketOrderSeq = ticketOrderFacade.createTicketOrder(dto, memberSeq);
                        log.info("예매 성공 ticketOrderSeq : {}", ticketOrderSeq);
                    } catch (Exception e) {
                        log.error("ticket order test fail : {}", e.getMessage());
                        failCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }

                });
            }
            latch.await();
            Assertions.assertNotNull(ticketOrderSeq);
        }


        @Order(2)
        @DisplayName("요청한 ticketOrderSeq와 ticket테이블의 orderSeq들이 같다.")
        @Test
        void ticketOrderSeqNotNullTest() {
            List<Ticket> ticketsByTicketSeqList = ticketService.getTicketsByTicketSeqList(ticketSeqList);
            Assertions.assertAll("Ticket's orderSeq Equals TicketOrders' seq",
                    () -> {
                        for (Ticket ticket : ticketsByTicketSeqList) {
                            Assertions.assertEquals(ticket.getOrderSeq(), ticketOrderSeq);
                        }
                    }
            );
        }

        @Order(3)
        @DisplayName("요청한 ticket 수와 예매된 티켓수가 일치한다.")
        @Test
        void ticketOrderInsertSizeTest() throws Exception {
            long orderCount = ticketService.getTicketsByTicketSeqList(ticketSeqList)
                    .stream()
                    .filter(t -> Objects.nonNull(t.getOrderSeq())).count();
            Assertions.assertEquals(orderCount, ticketSeqList.size());
        }

        @Order(4)
        @DisplayName("threadCount -1 개수는 티켓 예매 실패 count와 같다.")
        @Test
        void ticketOrderFailCountTest() {
            Assertions.assertEquals(threadCount -1, failCount.get());
        }
    }
}