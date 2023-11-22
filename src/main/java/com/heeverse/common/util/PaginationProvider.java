package com.heeverse.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Paging Offset 생성
 * @author gutenlee
 * @since 2023/10/27
 */
@UtilityClass
public class PaginationProvider {

    public static List<List<Long>> toChunk(List<Long> origin, int chunkSize) {
        return IntStream.range(0, (origin.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> origin.subList(i * chunkSize, Math.min(origin.size(), (i + 1) * chunkSize)))
                .toList();
    }

}
