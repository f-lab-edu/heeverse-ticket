package com.heeverse.ticket_order.service;

import com.heeverse.common.factory.TicketLogFactory;
import com.heeverse.common.factory.TicketOrderingDto;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author gutenlee
 * @since 2023/10/14
 */
@ActiveProfiles("dev-test")
@SpringBootTest
public class TicketOrderIntegrationTest_aggregation {

    @Autowired
    private QueryAggregationService aggregationService;
    @Autowired
    private TicketLogFactory ticketLogFactory;
    @MockBean
    private TicketOrderMapper ticketOrderMapper;
    @MockBean
    private TicketService ticketService;


    @Test
    @DisplayName("티켓 예매시 예매 시도한 티켓 개수와 저장된 로그 개수는 일치한다")
    void ticketOrderEventTest() throws Exception {

        // given
        TicketOrderingDto orderInfo = ticketLogFactory.givenTicketOrder();

        // when
        ticketLogFactory.whenStartTicketOrder(orderInfo.getCreatedTicketSeqList(), orderInfo.getMemberSeq());

        // then
        List<AggregateDto.Response> aggregated
                = aggregationService.aggregate(new AggregateDto.Request(orderInfo.getConcertSeq(), false));

        Assertions.assertAll(
                () -> assertEquals(
                        orderInfo.getCreatedTicketSeqList().size(),
                        getSumOrderTry(aggregated)),
                () -> ticketLogFactory.afterTestDeleteData(orderInfo)
        );
    }

    @Test
    @DisplayName("ticker order가 실패하면 집계 결과는 0 이다")
    void ticketOrderEventFailTest() throws Exception {
        // given
        TicketOrderingDto orderInfo = ticketLogFactory.givenTicketOrder();


        // when
        doThrow(new RuntimeException("실패 예약")).when(ticketOrderMapper).insertTicketOrder(any());

        Assertions.assertThrowsExactly(TicketingFailException.class,
            () -> ticketLogFactory.whenStartTicketOrder(orderInfo.getCreatedTicketSeqList(), orderInfo.getMemberSeq())
        );

        // then
        List<AggregateDto.Response> aggregated
                = aggregationService.aggregate(new AggregateDto.Request(orderInfo.getConcertSeq(), false));

        final int ZERO = 0;
        Assertions.assertAll(
                () -> assertEquals(ZERO, getSumOrderTry(aggregated)),
                () -> ticketLogFactory.afterTestDeleteData(orderInfo)
        );
    }

    @Test
    @DisplayName("ticker order가 실패하면 집계 결과는 0 이다 - 2")
    void ticketOrderEventFailTest_AfterTransactionEvent() throws Exception {
        // given
        TicketOrderingDto orderInfo = ticketLogFactory.givenTicketOrder();


        // when
        doThrow(new RuntimeException("실패 예약")).when(ticketService).getTicketLock(any());

        Assertions.assertThrowsExactly(TicketingFailException.class,
                () -> ticketLogFactory.whenStartTicketOrder(orderInfo.getCreatedTicketSeqList(), orderInfo.getMemberSeq())
        );

        // then
        List<AggregateDto.Response> aggregated
                = aggregationService.aggregate(new AggregateDto.Request(orderInfo.getConcertSeq(), false));

        final int ZERO = 0;
        Assertions.assertAll(
                () -> assertEquals(ZERO, getSumOrderTry(aggregated)),
                () -> ticketLogFactory.afterTestDeleteData(orderInfo)
        );
    }


    private static int getSumOrderTry(List<AggregateDto.Response> aggregated) {
        return aggregated.stream()
                .filter(r -> "ALL".equals(r.getGradeName()))
                .map(AggregateDto.Response::getOrderTry)
                .mapToInt(r -> r)
                .sum();
    }


}
