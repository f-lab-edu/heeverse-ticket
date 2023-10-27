package com.heeverse.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.LongStream;

/**
 * Paging Offset 생성
 * @author gutenlee
 * @since 2023/10/27
 */
@UtilityClass
public class PaginationProvider {

    public static List<Long> getOffset(
            long start,
            long to,
            long size
    ) {
        if (isLessThanSize(start, to, size)) {
            return List.of(start);
        }
        return collectOffset(size, start, to);
    }

    private static boolean isLessThanSize(long start, long to, long size) {
        return (to - start) <= size;
    }

    private static List<Long> collectOffset(long interval, long start, long to) {
        return LongStream.iterate(start, i -> i <= to, i -> i + interval)
                .boxed().toList();
    }
}
