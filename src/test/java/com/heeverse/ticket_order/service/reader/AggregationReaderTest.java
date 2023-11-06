package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadStrategy;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@ActiveProfiles("dev-test")
@SpringBootTest
@EnabledOnOs({OS.MAC})
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
    private static AggregateSelectMapperDto.Request request;

    @BeforeEach
    void setUp() {
        request = new AggregateSelectMapperDto.Request(1L);
    }

    @Test
    @DisplayName("멀티스레드로 집계")
    void multithreadingAggregationTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(multithreadingStrategy, request);
            log.info( "===== {} ====", i);
        }

        latch.await();
    }

    @Test
    @DisplayName("스트림으로 집계")
    void streamAggregationTest() throws Exception {

        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(streamAggregationStrategy, request);
            log.info( "===== {} ====", i);
        }
    }


    @Test
    @DisplayName("싱글 스레드로 집계 처리")
    void synchronousAggregationTest() throws Exception {

        for (int i = 0; i < LOOP; i++) {
            reader.doAggregation(singleThreadStrategy, request);
            log.info( "===== {} ====", i);
        }
    }

    @Test
    @DisplayName("비정규화 테이블에서 처리")
    void queryAggrTest() throws Exception {
        for (int i = 0; i < QUERY_LOOP; i++) {
            List<AggregateSelectMapperDto.Response> deNormalization
                    = aggregationMapper.selectGroupByGradeNameDeNormalization(1L);
            System.out.println("deNormalization = " + deNormalization);
            log.info("{}", i);
        }
    }

    @Test
    @DisplayName("정규화 테이블에서 처리")
    void queryAggrNormalizationTest() throws Exception {
        for (int i = 0; i < QUERY_LOOP; i++) {
            List<AggregateSelectMapperDto.Response> normalization
                    = aggregationMapper.selectGroupByGradeName(1L);
            log.info("{}", i);
        }
    }


    @Test
    @DisplayName("테스트용 데이터 넣기")
    void insert() throws Exception {

        int KSPO_DOME_TICKETS = 7_500;
        int tryOrderPerTicket = 266;
        String[] grades = new String[] {"VIP", "S", "R"};

        for (int i = 0; i < KSPO_DOME_TICKETS; i++) {
            ArrayList<TicketOrderLog> ticketOrderLogs = new ArrayList<>(tryOrderPerTicket);
            int ticketSeq = ThreadLocalRandom.current().nextInt(1, KSPO_DOME_TICKETS);
            int memberSeq = ThreadLocalRandom.current().nextInt(1, 1_000_000);
            for (int j = 0; j < tryOrderPerTicket; j++) {
                ticketOrderLogs.add(new TicketOrderLog(
                        ticketSeq, memberSeq, 12, 1,
                        grades[ThreadLocalRandom.current().nextInt(0, 3)])
                );
            }
            logMapper.insertTicketOrderLogDeNormalization(ticketOrderLogs);
            logMapper.insertTicketOrderLog(ticketOrderLogs);
        }
    }



}