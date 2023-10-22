package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadingStrategy;
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
    private MultiAggregationReader reader;
    @Autowired
    private SingleAggregationReader singleReader;
    @Autowired
    private StreamAggregationReader streamReader;

    private Logger log = LoggerFactory.getLogger(AggregationReaderTest.class);
    private static final int LOOP = 1;
    private static AggregateSelectMapperDto.Request request;

    @BeforeEach
    void setUp() {
        request = new AggregateSelectMapperDto.Request(1L);
    }

    @Test
    @DisplayName("멀티스레드로 집계")
    void multithreadingAggregationTest() throws Exception {

        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> responseList
                    = reader.getResultGroupByGrade(request, new MultithreadingStrategy(aggregationMapper));
            log.info( "===== {} ====", i);
        }
    }

    @Test
    @DisplayName("스트림으로 집계")
    void streamAggregationTest() throws Exception {

        for (int i = 0; i < LOOP; i++) {
            streamReader.getResultGroupByGrade(request);
            log.info( "===== {} ====", i);
        }
    }


    @Test
    @DisplayName("싱글 스레드로 집계 처리")
    void synchronousAggregationTest() throws Exception {

        // DB CPU 25%
        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> responseList
                    = singleReader.getResultGroupByGrade(request, new SingleThreadingStrategy(aggregationMapper));
            log.info( "===== {} ====", i);
        }
    }

    @Test
    @DisplayName("비정규화 테이블에서 처리")
    void queryAggrTest() throws Exception {
        // DB CPU 38%, TPS 6/sec
        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> deNormalization
                    = aggregationMapper.selectGroupByGradeNameDeNormalization(1L);
            log.info("{}", i);
        }
    }

    @Test
    @DisplayName("정규화 테이블에서 처리")
    void queryAggrNormalizationTest() throws Exception {
        //  CPU 86%, TPS 1.3/sec
        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> normalization
                    = aggregationMapper.selectGroupByGradeName(1L);
            log.info("{}", i);
        }
    }


    @Test
    @DisplayName("테스트용 데이터 넣기")
    void insert() throws Exception {

        int KSPO_DOME_TICKETS = 10_000;
        int tryOrderPerTicket = 500;
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
        }
    }



}