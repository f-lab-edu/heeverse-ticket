package com.heeverse.ticket.service;

import ch.qos.logback.classic.Logger;
import com.heeverse.common.IntegrationTestService;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import com.heeverse.ticket_order.service.TicketOrderTestHelper;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.heeverse.ticket_order.service.TicketOrderTestHelper.createTicketOrderRequestDto;

/**
 * @author gutenlee
 * @since 2023/10/06
 */
@Disabled
public class TicketIntegrationTest_aggregation extends IntegrationTestService {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketOrderFacade ticketOrderFacade;

    private final Logger log = (Logger) LoggerFactory.getLogger(TicketIntegrationTest_aggregation.class);


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
