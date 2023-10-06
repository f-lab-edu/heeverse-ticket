package com.heeverse.ticket_order.service;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.common.IntegrationTestService;
import com.heeverse.common.factory.ConcertFactory;
import com.heeverse.common.factory.TicketFactory;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.heeverse.ticket_order.service.TicketOrderTestHelper.createTicketOrderRequestDto;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jeongheekim
 * @date 2023/09/10
 */

class TicketOrderFacadeTest extends IntegrationTestService {

    @Autowired
    protected ConcertFactory concertFactory;
    @Autowired
    protected TicketFactory ticketFactory;
    @Autowired
    private TicketOrderFacade ticketOrderFacade;

    @DisplayName("이미 예매한 티켓이 없으면 티켓예매는 성공한다.")
    @Test
    void ticketOrderSuccessTest() throws Exception {
        int failCount = 0;
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());

        List<TicketOrderResponseDto> dtoList = orderTicket(ticketList, memberSeq);
        long ticketOrderFailCount = dtoList.stream()
                .filter(t -> t.getBookingStatus().equals(BookingStatus.FAIL.getDescription()))
                .count();
        Assertions.assertEquals(failCount, ticketOrderFailCount);
    }

    @DisplayName("티켓예매가 성공하면 리턴값은 NOTNULL이다.")
    @Test
    void ticketOrderSuccessResponseNotNullTest() throws Exception {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());

        assertNotNull(orderTicket(ticketList, memberSeq));
    }

    @DisplayName("이미 예매한 티켓을 예매하면 실패한다.")
    @Test
    void ticketOrderFailTest() throws Exception {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());

        orderTicket(ticketList, memberSeq);
        Assertions.assertThrows(TicketingFailException.class, () -> orderTicket(ticketList, memberSeq));
    }

    @DisplayName("티켓 예매 성공하면 예매 정보가 조회된다.")
    @Test
    void ticketOrderSuccessResponseTest() throws Exception {
        List<Long> ticketList = createTicket(createConcert());
        Long memberSeq = memberFactory.createMember(MemberTestHelper.getMockMember());

        List<TicketOrderResponseDto> dtoList = orderTicket(ticketList, memberSeq);
        Assertions.assertAll(
                () -> dtoList.forEach(dto -> Assertions.assertEquals(dto.getBookingStatus(), BookingStatus.SUCCESS.getDescription())),
                () -> dtoList.forEach(dto -> Assertions.assertNotNull(dto.getTicketSerialNumber())),
                () -> dtoList.forEach(dto -> Assertions.assertTrue(dto.getBookingDate().isBefore(LocalDateTime.now()))),
                () -> dtoList.forEach(dto -> Assertions.assertNotNull(dto.getGradeName())),
                () -> dtoList.forEach(dto -> Assertions.assertNotNull(dto.getConcertName()))
        );
    }

    private Long createConcert() {
        concertFactory.registerConcert(ConcertHelper.buildConcert());
        return concertFactory.selectLatestConcertSeq();
    }

    private List<Long> createTicket(Long concertSeq) {
        List<GradeTicket> gradeTickets = TicketTestHelper.buildTicketGrade(concertSeq);
        ticketFactory.insertTicketGrade(gradeTickets);

        List<Ticket> ticketList = gradeTickets.stream().map(gradeTicket ->
                TicketTestHelper.buildTicket(concertSeq, gradeTicket)).toList();
        ticketFactory.insertTicket(ticketList);

        List<Ticket> tickets = ticketFactory.selectTicketSeqList(concertSeq);
        return tickets.stream().map(Ticket::getSeq).collect(Collectors.toList());
    }

    private List<TicketOrderResponseDto> orderTicket(List<Long> ticketList, Long memberSeq) {
        return ticketOrderFacade.startTicketOrderJob(TicketOrderTestHelper.createTicketOrderRequestDto(ticketList), memberSeq);
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