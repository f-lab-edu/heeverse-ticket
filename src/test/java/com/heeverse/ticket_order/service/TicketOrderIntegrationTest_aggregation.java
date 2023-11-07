package com.heeverse.ticket_order.service;

import com.heeverse.common.factory.TicketLogFactory;
import com.heeverse.common.factory.TicketOrderingDto;
import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private MultithreadingAggregationService multithreadingAggregationService;
    @Autowired
    private TicketLogFactory ticketLogFactory;


    @Test
    @DisplayName("티켓 예매시 예매 시도한 티켓 개수와 저장된 로그 개수는 일치한다")
    void ticketOrderEventTest() throws Exception {

        // given
        TicketOrderingDto orderInfo = ticketLogFactory.givenTicketOrder();

        // when
        ticketLogFactory.whenStartTicketOrder(orderInfo.getCreatedTicketSeqList(), orderInfo.getMemberSeq());

        // then
        List<AggregateDto.Response> aggregated
                = aggregationService.aggregate(new AggregateSelectMapperDto.Request(orderInfo.getConcertSeq(), null), false);

        Assertions.assertAll(
                () -> assertEquals(
                        orderInfo.getCreatedTicketSeqList().size(),
                        getSumOrderTry(aggregated)),
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
