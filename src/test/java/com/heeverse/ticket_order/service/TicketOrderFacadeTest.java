package com.heeverse.ticket_order.service;

import com.heeverse.common.IntegrationTestService;
import com.heeverse.common.factory.ConcertFactory;
import com.heeverse.common.factory.TicketFactory;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

}