package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@DisplayName("병렬 프로그래밍의 ResultMap 안전성 단위 테스트")
class ResultMapUnitTest {

    private final static Logger log = LoggerFactory.getLogger(ResultMapUnitTest.class);
    private final static int POOL_SIZE = 10;
    private final static ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);


    @Test
    @DisplayName("새로운 key 인 경우 add 테스트")
    void addTest() {
         //given
        final String gradeName = "A";
        final var entry = Map.entry(gradeName, 100L);
        final ResultConcurrentMap resultConcurrentMap = new ResultConcurrentMap();

        // when
        resultConcurrentMap.add(entry);

        // then
        assertOrderTryOfGrade(
            resultConcurrentMap.toList(1L),
            gradeName,
            (c) -> Assertions.assertEquals(entry.getValue(), c.getOrderTry())
        );
    }

    @Test
    @DisplayName("존재하는 key 인 경우 add 테스트")
    void addWhenExistKeyTest() {
        //given
        final String gradeName = "A";
        final var entry = Map.entry(gradeName, 100L);
        final ResultConcurrentMap resultConcurrentMap = new ResultConcurrentMap();

        // when
        int loop = 2;
        for (int i = 0; i < loop; i++) {
            resultConcurrentMap.add(entry);
        }

        // then
        assertOrderTryOfGrade(
            resultConcurrentMap.toList(1L),
            gradeName,
            (c) -> Assertions.assertEquals(entry.getValue() * loop, c.getOrderTry())
        );
    }



    @Test
    @DisplayName("ResultConcurrentMap 동기 처리 테스트")
    void resultConcurrentMapSynchronousTest() {
        // given
        final ResultConcurrentMap resultConcurrentMap = new ResultConcurrentMap();

        // when
        final String[] keys = {"A", "B", "C", "D"};

        final int start = 1;
        final int inclusiveEnd = 50;
        addSynchronous(resultConcurrentMap, keys, start, inclusiveEnd);

        // then
        for (AggregateInsertMapperDto dto : resultConcurrentMap.toList(1L)) {
            Assertions.assertEquals(addFromAtoZ(0, start, inclusiveEnd), dto.getOrderTry());
        }
    }


    @RepeatedTest(100)
    @DisplayName("ResultConcurrentMap thread-safe 테스트")
    void resultMapThreadSafeTest() {

        // given
        final ResultConcurrentMap resultConcurrentMap = new ResultConcurrentMap();
        final String[] keys = {"A", "B", "C", "D"};

        // when
        final int start = 1;
        final int inclusiveEnd = 200;
        try {
            addConcurrently(resultConcurrentMap, keys, start, inclusiveEnd);
        } catch (InterruptedException e) {
            log.error("데드락으로 인한 타임아웃 발생");
            throw new RuntimeException(e);
        }

        // then
        for (AggregateInsertMapperDto dto : resultConcurrentMap.toList(1L)) {
            Assertions.assertEquals(addFromAtoZ(0, start, inclusiveEnd), dto.getOrderTry());
        }
    }

    @RepeatedTest(10)
    @DisplayName("resultHashMap 은 NOT thread-safe 하다. ConcurrentModificationException or IllegalStateException or InterruptedException 발생한다")
    void resultHashMapThreadNotSafeTest() {

        // given
        final ResultHashMap resultHashMap = new ResultHashMap();
        final String[] keys = {"A", "B", "C", "D"};

        List<Class<? extends Exception>> exceptionsWhenConcurrent = List.of(
                ConcurrentModificationException.class,
                IllegalStateException.class,
                InterruptedException.class
        );
        // when
        final int start = 1;
        final int inclusiveEnd = 220;

        try {
            addConcurrently(resultHashMap, keys, start, inclusiveEnd);
            resultHashMap.toList(1L);
        } catch (Exception e) {
            Assertions.assertTrue(
                    exceptionsWhenConcurrent.stream()
                            .anyMatch(candidate -> candidate.isNestmateOf(e.getClass()))
            );
        }

    }

    private static void addSynchronous(ResultMap resultMap, String[] keys, int start, int inclusiveEnd) {
        for (String key : keys) {
            for (int i = start; i <= inclusiveEnd; i++) {
                resultMap.add(Map.entry(key, (long) i));
            }
        }
    }

    private static void addConcurrently(ResultMap resultMap, String[] keys, int start, int inclusiveEnd)
            throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(keys.length * inclusiveEnd);

        for (String key : keys) {
            for (int i = start; i <= inclusiveEnd; i++) {
                int finalI = i;
                es.execute(() -> {
                    resultMap.add(Map.entry(key, (long) finalI));
                    latch.countDown();
                });
            }
        }

        latch.await(500, TimeUnit.MILLISECONDS);
    }

    private void assertOrderTryOfGrade(List<AggregateInsertMapperDto> list, String key, Consumer<AggregateInsertMapperDto> assertConsumer) {
        list.stream()
                .filter(r -> key.equals(r.getGradeName()))
                .findFirst()
                .ifPresent(assertConsumer);
    }


    public long addFromAtoZ(long init, long a, long z) {
        for (long i = a; i <= z; i++) {
            init += i;
        }
        return init;
    }


}