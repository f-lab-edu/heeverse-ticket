package com.heeverse.ticket_order.service.reader;

import com.heeverse.common.factory.TicketLogFactory;
import com.heeverse.common.factory.TicketOrderingDto;
import com.heeverse.ticket_order.domain.dto.StrategyDto;
import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadStrategy;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ActiveProfiles(profiles = "local")
@SpringBootTest
class AggregationReaderTest {


    @Autowired
    private TicketOrderAggregationMapper aggregationMapper;
    @Autowired
    private CommonAggregationReader reader;

    @Autowired
    private MultithreadingStrategy multithreadingStrategy;
    @Autowired
    private SingleThreadStrategy singleThreadStrategy;
    @Autowired
    private StreamAggregationStrategy streamAggregationStrategy;

    @Autowired
    private TicketLogFactory ticketLogFactory;

    private Logger log = LoggerFactory.getLogger(AggregationReaderTest.class);
    private static final int PAGE_SIZE = 100;
    private static final ExecutorService es = Executors.newFixedThreadPool(1);


    @Test
    @DisplayName("멀티스레드로 집계")
    void multithreadingAggregationTest() throws Exception {

        //given
        OrderLog saved = insert();

        // when
        CountDownLatch latch = new CountDownLatch(1);

        var request = new AggregateSelectMapperDto.Request(saved.concertSeq(), new StrategyDto(StrategyType.MULTI_THREAD, PAGE_SIZE));
        CompletableFuture.runAsync(() -> reader.doAggregation(multithreadingStrategy, request), es)
                .thenAccept(r -> latch.countDown());

        latch.await();

        // then
        List<AggregateSelectMapperDto.SimpleResponse> response = ticketLogFactory.selectLog(saved.ticketList);

        Assertions.assertEquals(response.size(), saved.ticketList().size());
    }

    @Test
    @DisplayName("[스트림 처리] 예매 시도 log와 집계 결과 order try 값은 일치한다")
    void streamAggregationTest() throws InterruptedException {

        //given
        OrderLog saved = insert();

        // when
        var request = new AggregateSelectMapperDto.Request(saved.concertSeq(), new StrategyDto(StrategyType.STREAM, PAGE_SIZE));
        reader.doAggregation(streamAggregationStrategy, request);

        // then
        List<AggregateSelectMapperDto.Response> responses = ticketLogFactory.selectAggregateResult(saved.concertSeq());
        int sum = responses.stream().mapToInt(AggregateSelectMapperDto.Response::orderTry).sum();

        Assertions.assertEquals(saved.ticketList().size(), sum);

    }

    @Test
    @DisplayName("[싱글 스레드] 예매 시도 log와 집계 결과 order try 값은 일치한다")
    void synchronousAggregationTest() throws InterruptedException {

        //given
        OrderLog saved = insert();

        // when
        var request = new AggregateSelectMapperDto.Request(saved.concertSeq(), new StrategyDto(StrategyType.STREAM, PAGE_SIZE));
        reader.doAggregation(singleThreadStrategy, request);

        // then
        List<AggregateSelectMapperDto.Response> responses = ticketLogFactory.selectAggregateResult(saved.concertSeq());
        int sum = responses.stream().mapToInt(AggregateSelectMapperDto.Response::orderTry).sum();

        Assertions.assertEquals(saved.ticketList().size(), sum);

    }

    public OrderLog insert() {
        TicketOrderingDto orderInfo = ticketLogFactory.givenTicketOrder();
        ticketLogFactory.whenStartTicketOrder(orderInfo.getCreatedTicketSeqList(), orderInfo.getMemberSeq());

        return new OrderLog(orderInfo.getCreatedTicketSeqList(), orderInfo.getConcertSeq());
    }

    private record OrderLog(List<Long> ticketList, long concertSeq) {

    }

}