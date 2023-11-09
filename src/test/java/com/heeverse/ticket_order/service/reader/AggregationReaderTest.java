package com.heeverse.ticket_order.service.reader;

import com.heeverse.common.factory.TicketLogFactory;
import com.heeverse.common.factory.TicketOrderingDto;
import com.heeverse.common.util.PrimitiveUtils;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket_order.domain.dto.StrategyDto;
import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadStrategy;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@ActiveProfiles("local")
@SpringBootTest
@Disabled
class AggregationReaderTest {


    @Autowired
    private TicketOrderAggregationMapper aggregationMapper;
    @Autowired
    private TicketOrderLogMapper logMapper;
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
        List<TicketOrderLog> saved = insert();
        CountDownLatch latch = new CountDownLatch(1);
        var request = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.MULTI_THREAD, PAGE_SIZE));

        CompletableFuture.runAsync(() -> reader.doAggregation(multithreadingStrategy, request), es)
                .thenAccept(r -> latch.countDown());

        latch.await();

        // then
        List<AggregateSelectMapperDto.SimpleResponse> response
                = null;
        try {
            response = aggregationMapper.selectTicketSeqWhereIn(saved.stream().map(TicketOrderLog::getTicketSeq).toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(response.size(), saved.size());

    }

    @Test
    @DisplayName("스트림으로 집계")
    void streamAggregationTest() throws InterruptedException {

        //given
        List<TicketOrderLog> saved = insert();
        var request = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.STREAM, PAGE_SIZE));


        // when
        reader.doAggregation(streamAggregationStrategy, request);

        // then
        List<AggregateSelectMapperDto.SimpleResponse> response
                = aggregationMapper.selectTicketSeqWhereIn(saved.stream().map(TicketOrderLog::getTicketSeq).toList());

        Assertions.assertEquals(response.size(), saved.size());
    }

    @Test
    @DisplayName("싱글 스레드로 집계 처리")
    void synchronousAggregationTest() throws InterruptedException {

        //given
        List<TicketOrderLog> saved = insert();
        var request = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.STREAM, PAGE_SIZE));

        // when
        reader.doAggregation(singleThreadStrategy, request);

        // then
        List<AggregateSelectMapperDto.SimpleResponse> response
                = aggregationMapper.selectTicketSeqWhereIn(saved.stream().map(TicketOrderLog::getTicketSeq).toList());

    }


    private List<TicketOrderLog> insert() {

        TicketOrderingDto orderingDto = ticketLogFactory.givenTicketOrder();

        int KSPO_DOME_TICKETS = 100;
        int tryOrderPerTicket = 5;
        List<GradeTicket> gradeTicketList = orderingDto.getGradeTicketList();

        List<TicketOrderLog> saved = new ArrayList<>();

        for (int i = 0; i < KSPO_DOME_TICKETS; i++) {
            List<TicketOrderLog> ticketOrderLogs = getTicketOrderLogs(orderingDto, gradeTicketList, tryOrderPerTicket);
            logMapper.insertTicketOrderLogDeNormalization(ticketOrderLogs);
            logMapper.insertTicketOrderLog(ticketOrderLogs);

            saved.addAll(ticketOrderLogs);
        }

        return saved;
    }


    private static List<TicketOrderLog> getTicketOrderLogs(
            TicketOrderingDto orderingDto,
            List<GradeTicket> gradeTicketList,
            int tryOrderPerTicket
    ) {

        long minTicket = TicketUtils.minSeq(orderingDto.getTicketList());
        long maxTicket = TicketUtils.maxSeq(orderingDto.getTicketList());
        int ticketSeq = ThreadLocalRandom.current().nextInt(PrimitiveUtils.toIntSafely(minTicket), PrimitiveUtils.toIntSafely(maxTicket));

        return IntStream.range(0, tryOrderPerTicket)
                .mapToObj(r -> new TicketOrderLog(
                        ticketSeq,
                        orderingDto.getMemberSeq(),
                        12,
                        orderingDto.getConcertSeq(),
                        gradeTicketList.get(ThreadLocalRandom.current().nextInt(0, gradeTicketList.size())).getGradeName()
                )).toList();
    }


}