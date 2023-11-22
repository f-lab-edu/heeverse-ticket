package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gutenlee
 * @since 2023/11/03
 */
public class StreamHelper {

    public static <K, V> Stream<Map.Entry<K, V>> toEntrySetStream(Map<K, V> map) {
        if (map == null) {
            return Stream.empty();
        }

        return map.entrySet().stream();
    }

    public static Collector<AggregateSelectMapperDto.SimpleResponse, ?, Map<String, Long>> countGroupingByKey(GradeInfo gradeInfo) {
        return Collectors.groupingBy(
                simpleResponse -> gradeInfo.getGrade(simpleResponse.ticketSeq()),
                Collectors.counting());
    }
}
