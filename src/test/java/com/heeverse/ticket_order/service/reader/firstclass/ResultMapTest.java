package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ResultMapTest {

    private final Logger log = LoggerFactory.getLogger(ResultMapTest.class);
    private final static ExecutorService es = Executors.newFixedThreadPool(1);


    @Test
    @DisplayName("새로운 key 인 경우 add 테스트")
    void addTest() {
         //given
        String key = "A";
        Map.Entry<String, Long> entry = Map.entry(key, 100L);
        ResultConcurrentMap resultMap = new ResultConcurrentMap(new ConcurrentHashMap<>());

        // when
        resultMap.add(entry);

        // then
        resultMap.toList(1L)
                .stream()
                .filter(r -> key.equals(r.getGradeName()))
                .findFirst()
                .ifPresent(
                    c -> Assertions.assertEquals(entry.getValue(), c.getOrderTry())
                );
    }

    @Test
    @DisplayName("존재하는 key 인 경우 add 테스트")
    void addWhenExistKeyTest() {
        //given
        String key = "A";
        Map.Entry<String, Long> entry = Map.entry(key, 100L);
        ResultConcurrentMap resultMap = new ResultConcurrentMap(new ConcurrentHashMap<>());

        // when
        int loop = 2;
        for (int i = 0; i < loop; i++) {
            resultMap.add(entry);
        }

        // then
        resultMap.toList(1L)
                .stream()
                .filter(r -> key.equals(r.getGradeName()))
                .findFirst()
                .ifPresent(
                        c -> Assertions.assertEquals(entry.getValue() * loop, c.getOrderTry())
                );
    }



    @Test
    @DisplayName("ResultMap 연산 동기 처리 테스트 - 1~50까지 합은 1275 여야 한다")
    void 동기처리_테스트() throws Exception {
        // given
        ResultConcurrentMap resultMap = new ResultConcurrentMap(new ConcurrentHashMap<>());

        // when
        String[] keys = {"A", "B", "C", "D"};

        for (String key : keys) {
            for (int i = 1; i <= 50; i++) {
                resultMap.add(Map.entry(key, (long) i));
            }
        }

        // then
        List<AggregateInsertMapperDto> list = resultMap.toList(1L);
        for (AggregateInsertMapperDto dto : list) {
            Assertions.assertEquals(addFromAtoZ(0, 1, 50), dto.getOrderTry());
        }

    }


    @Test
    @DisplayName("ResultMap 연산 thread-safe 테스트 - 1~50까지 합은 1275 여야 한다")
    void resultMapThreadSafeTest() throws Exception {

        // given
        ResultConcurrentMap resultMap = new ResultConcurrentMap(new ConcurrentHashMap<>());

        // when
        String[] keys = {"A", "B", "C", "D"};

        CyclicBarrier barrier = new CyclicBarrier(keys.length * 50);
        for (String key : keys) {
            for (int i = 1; i <= 50; i++) {
                int finalI = i;
                es.execute(() -> {
                    resultMap.add(Map.entry(key, (long) finalI));
                });
            }
        }

        // then
        List<AggregateInsertMapperDto> list = resultMap.toList(1L);
        for (AggregateInsertMapperDto dto : list) {
            log.info(dto.toString());
            Assertions.assertEquals(addFromAtoZ(0, 1, 50), dto.getOrderTry());
        }

    }


    public long addFromAtoZ(long init, long a, long z) {
        for (long i = a; i <= z; i++) {
            init += i;
        }
        return init;
    }


}