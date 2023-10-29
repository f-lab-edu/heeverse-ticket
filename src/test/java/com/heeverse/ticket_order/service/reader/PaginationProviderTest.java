package com.heeverse.ticket_order.service.reader;

import com.heeverse.common.util.PaginationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;

/**
 * @author gutenlee
 * @since 2023/10/27
 */
public class PaginationProviderTest {

    @Test
    @DisplayName("리스트를 chuckSize 만큼 sublist로 분할하면 개수는 (list size / size) 결과의 올림한 값과 같아야 한다")
    void chunkTest() throws Exception {

        // given
        final int chuckSize = 3;
        final List<Long> list = LongStream.rangeClosed(1, 7).boxed().toList();

        // when
        List<List<Long>> chunk = PaginationProvider.toChunk(list, chuckSize);

        // then
        Assertions.assertEquals(Math.ceil((double) list.size() / chuckSize), chunk.size());
    }





}
