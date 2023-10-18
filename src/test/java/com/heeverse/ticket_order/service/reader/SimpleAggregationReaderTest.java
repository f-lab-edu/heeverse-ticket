package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
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
class SimpleAggregationReaderTest {

    @Autowired
    private TicketOrderAggregationMapper aggregationMapper;
    @Autowired
    private TicketOrderLogMapper logMapper;
    @Autowired
    private SimpleAggregationReader reader;

    private Logger log = LoggerFactory.getLogger(SimpleAggregationReaderTest.class);
    private static final int LOOP = 100;
    private static final int THREAD_POOL_SIZE = 100;


    @Test
    @DisplayName("멀티스레드로 집계")
    void multithreadingAggregationTest() throws Exception {

        //  1 min 40 sec, DB CPU 33%
        // 1 min 33 sec,  DB CPU 33%
        reader.setStrategy(new FirstMultithreadingStrategy(aggregationMapper));

        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> responseList
                    = reader.getResultGroupByGrade(new AggregateSelectMapperDto.Request(1L));
            log.info( "===== {} ====", i);
        }
    }

    @Test
    @DisplayName("동기적으로 집계 처리")
    void synchronousAggregationTest() throws Exception {

        reader.setStrategy(new NonMultithreadingStrategy(aggregationMapper));
        // DB CPU 25%
        for (int i = 0; i < LOOP; i++) {
            List<AggregateSelectMapperDto.Response> responseList
                    = reader.getResultGroupByGrade(new AggregateSelectMapperDto.Request(1L));
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

         int dome = 7500;
        for (int i = 0; i < dome; i++) {
            ArrayList<TicketOrderLog> list = new ArrayList<>(100);
            int tryByTicket = ThreadLocalRandom.current().nextInt(50, 120);
            for (int j = 0; j < tryByTicket; j++) {
                list.add(new TicketOrderLog(i, 1, 12, 1));
            }
            logMapper.insertTicketOrderLogDeNormalization(list);
        }
    }



}