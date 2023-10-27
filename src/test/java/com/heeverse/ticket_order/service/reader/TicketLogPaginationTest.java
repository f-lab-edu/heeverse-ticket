package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.common.util.PaginationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.*;

/**
 * @author gutenlee
 * @since 2023/10/27
 */
@SpringBootTest
@ActiveProfiles("dev-test")
public class TicketLogPaginationTest {


    @Autowired
    private TicketOrderAggregationMapper aggregationMapper;

    @TestFactory
    MinMaxResponse getMinMaxTest() throws Exception {
        // 코드 실행 시간 측정 시작
        long startTime = System.nanoTime();
        MinMaxResponse minMaxResponse = aggregationMapper.selectMinMax(
                ZeroOffsetRequest.start(1L, 1L, 2000L)
        );
        // 코드 실행 시간 측정 종료
        long endTime = System.nanoTime();
        // 실행 시간 계산 (나노초 단위)
        long timeElapsed = endTime - startTime;
        System.out.println("===== Elapsed : " + timeElapsed / 1000000);
        System.out.println("minMax = " + minMaxResponse);

        Assertions.assertNotNull(minMaxResponse);

        return minMaxResponse;
    }

    @Test
    @DisplayName("offset 계산 결과는 1개 이상이다")
    void pagingTest() throws Exception {

        MinMaxResponse minMax = getMinMaxTest();
        List<Long> longs =
                PaginationProvider.getOffset(10000, minMax.minSeq(), minMax.maxSeq());

        Assertions.assertTrue(0 < longs.size());
    }





}
