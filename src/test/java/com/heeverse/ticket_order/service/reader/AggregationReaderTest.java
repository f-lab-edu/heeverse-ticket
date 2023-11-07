package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.dto.StrategyDto;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadStrategy;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@ActiveProfiles("local")
@SpringBootTest
@EnabledOnOs(OS.MAC)
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

    private Logger log = LoggerFactory.getLogger(AggregationReaderTest.class);
    private static final int LOOP = 1;
    private static final int QUERY_LOOP = 1;
    private static final int PAGE_SIZE = 100;
    private static ArrayList<TicketOrderLog> saved;

    @BeforeEach
    void setUp() {
        saved = insert();
    }

    @Test
    @DisplayName("멀티스레드로 집계")
    void multithreadingAggregationTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        //given
        AggregateSelectMapperDto.Request request
                = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.MULTI_THREAD, PAGE_SIZE));

        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(multithreadingStrategy, request);
            log.info( "===== {} ====", i);
        }

        latch.await();
    }

    @Test
    @DisplayName("스트림으로 집계")
    void streamAggregationTest() {
        //given
        AggregateSelectMapperDto.Request request
                = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.STREAM, PAGE_SIZE));


        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(streamAggregationStrategy, request);
            log.info( "===== {} ====", i);
        }
    }


    @Test
    @DisplayName("싱글 스레드로 집계 처리")
    void synchronousAggregationTest() {

        //given
        AggregateSelectMapperDto.Request request
                = new AggregateSelectMapperDto.Request(1L, new StrategyDto(StrategyType.SINGLE_THREAD, PAGE_SIZE));

        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(singleThreadStrategy, request);
            log.info( "===== {} ====", i);
        }
    }

    @Test
    @DisplayName("비정규화 테이블에서 처리")
    void queryAggrTest() {
        //given
        AggregateSelectMapperDto.Request request
                = new AggregateSelectMapperDto.Request(1L,null);

        for (int i = 0; i < QUERY_LOOP; i++) {
            List<AggregateSelectMapperDto.Response> deNormalization
                    = aggregationMapper.selectGroupByGradeNameDeNormalization(request.concertSeq());
            System.out.println("deNormalization = " + deNormalization);
            log.info("{}", i);
        }
    }

    @Test
    @DisplayName("정규화 테이블에서 처리")
    void queryAggrNormalizationTest() {

        //given
        AggregateSelectMapperDto.Request request
                = new AggregateSelectMapperDto.Request(1L,null);

        for (int i = 0; i < QUERY_LOOP; i++) {
            List<AggregateSelectMapperDto.Response> normalization
                    = aggregationMapper.selectGroupByGradeName(request.concertSeq());
            log.info("{}", i);
        }
    }


    @TestFactory
    @DisplayName("테스트용 데이터 넣기")
    ArrayList<TicketOrderLog> insert() {

        int KSPO_DOME_TICKETS = 100;
        int tryOrderPerTicket = 20;
        String[] grades = new String[] {"VIP", "S", "R"};


        ArrayList<TicketOrderLog> saved = new ArrayList<>();

        for (int i = 0; i < KSPO_DOME_TICKETS; i++) {
            ArrayList<TicketOrderLog> ticketOrderLogs = new ArrayList<>(tryOrderPerTicket);
            int ticketSeq = ThreadLocalRandom.current().nextInt(1, KSPO_DOME_TICKETS);
            int memberSeq = ThreadLocalRandom.current().nextInt(1, 1_000_000);
            for (int j = 0; j < tryOrderPerTicket; j++) {
                ticketOrderLogs.add(new TicketOrderLog(
                        ticketSeq,
                        memberSeq,
                        12,
                        1,
                        grades[ThreadLocalRandom.current().nextInt(0, 3)])
                );
            }
            logMapper.insertTicketOrderLogDeNormalization(ticketOrderLogs);
            logMapper.insertTicketOrderLog(ticketOrderLogs);

            saved.addAll(ticketOrderLogs);
        }

        Assertions.assertTrue(saved.size() > 0);
        return saved;
    }



}