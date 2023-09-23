package com.heeverse.ticket_order.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.heeverse.ticket_order.service.TicketOrderTestHelper.createTicketOrderRequestDto;

/**
 * @author jeongheekim
 * @date 2023/09/10
 */
@ActiveProfiles("dev")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class TicketOrderFacadeTest {

    @Autowired
    private TicketOrderFacade ticketOrderFacade;

    @Autowired
    private TicketService ticketService;
    private static List<Long> ticketSeqList;
    private final int threadCount = 4;
    private final int threadPoolSize = 32;
    private AtomicInteger failCount = new AtomicInteger(0);

    @Transactional
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
        @DisplayName("동시에 요청한다.")
        @Order(1)
        void setUp() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            CountDownLatch latch = new CountDownLatch(threadCount);
            TicketOrderRequestDto dto = createTicketOrderRequestDto(ticketSeqList);
            Long memberSeq = 14L;
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        ticketOrderFacade.startTicketOrderJob(dto, memberSeq);
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

        @Order(2)
        @DisplayName("요청한 ticket 수와 예매된 티켓수가 일치한다.")
        @Test
        void ticketOrderInsertSizeTest() throws Exception {
            long orderCount = ticketService.getTicketsByTicketSeqList(ticketSeqList)
                    .stream()
                    .filter(t -> Objects.nonNull(t.getOrderSeq())).count();
            Assertions.assertEquals(orderCount, ticketSeqList.size());
        }

        @Order(3)
        @DisplayName("threadCount -1 개수는 티켓 예매 실패 count와 같다.")
        @Test
        void ticketOrderFailCountTest() {
            Assertions.assertEquals(threadCount -1, failCount.get());
        }


        @TestFactory
        private long getConcertSeq() {
            return 1;
        }

        @TestFactory
        private long getNotExistsConcertSeq() {
            return Long.MAX_VALUE;
        }

        @TestFactory
        private List<Long> getUnorderedTicket(long concertSeq) {
            List<Long> possibleTicketSeq = ticketService.getTicket(concertSeq)
                    .stream()
                    .filter(ticket -> ticket.getOrderSeq() == null)
                    .map(Ticket::getSeq)
                    .collect(Collectors.toList());

            Assertions.assertFalse(CollectionUtils.isEmpty(possibleTicketSeq));

            return possibleTicketSeq;
        }

        @TestFactory
        private void lockTicketRecord(List<Long> toOrderTicketSeq) throws Exception {

            TicketOrderRequestDto dto = createTicketOrderRequestDto(toOrderTicketSeq);
            Long memberSeq = 14L;

            ticketOrderFacade.startTicketOrderJob(dto, memberSeq).forEach(ordered -> {
                log.info("ordered : {}",  ordered.toString());
                Assertions.assertEquals(BookingStatus.SUCCESS.getDescription(), ordered.getBookingStatus());
            });

        }

        @Test
        @DisplayName("티켓 잔여 집계 성공")
        void ticketRemainsTest() throws Exception {
            List<TicketRemainsResponseDto> ticketRemains = ticketOrderFacade.getTicketRemains(new TicketRemainsDto(getConcertSeq()));

            Assertions.assertFalse(CollectionUtils.isEmpty(ticketRemains));
        }

        @Test
        @DisplayName("발행된 티켓이 없으면 집계 조회 실패한다")
        void ticketRemainsFailTest() throws Exception {
            Assertions.assertThrowsExactly(TicketAggregationFailException.class,
                    () -> ticketOrderFacade.getTicketRemains(new TicketRemainsDto(getNotExistsConcertSeq())
            ));
        }


        @Test
        @DisplayName("티켓 예매 진행중에 티켓 집계 쿼리 정상조회된다")
        void selectTicketRemainsWhenRecordLockedTest() throws Exception {

            List<List<Long>> subList = TicketOrderTestHelper.partitionSeq(getUnorderedTicket(getConcertSeq()), 3)
                    .subList(1, 100);

            int size = subList.size();
            CountDownLatch latch = new CountDownLatch(size * 2);
            ExecutorService executor = Executors.newScheduledThreadPool(10);
            ExecutorService aggrExecutor = Executors.newScheduledThreadPool(10);

            for (List<Long> toOrderTicketSeq : subList) {
                executor.execute(() -> {
                    try {
                        lockTicketRecord(toOrderTicketSeq);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            for (int i = 0; i < subList.size(); i++) {
                aggrExecutor.execute(() -> {
                    long start = System.nanoTime();
                    List<TicketRemainsResponseDto> ticketRemains = ticketOrderFacade.getTicketRemains(new TicketRemainsDto(getConcertSeq()));
                    for (TicketRemainsResponseDto ticketRemain : ticketRemains) {
                        log.info("등급 {} / 잔여 {}", ticketRemain.gradeTicket, ticketRemain.remain);
                    }
                    long end = System.nanoTime();

                    log.info("티켓 집계 조회 [" + 0 + "] : " + TimeUnit.MILLISECONDS.convert((end - start), TimeUnit.NANOSECONDS) + " millis");

                    latch.countDown();
                });
            }
            latch.await();

        }
    }
}