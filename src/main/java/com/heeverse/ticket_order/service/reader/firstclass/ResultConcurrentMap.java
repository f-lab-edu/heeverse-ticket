package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;

/**
 * @author gutenlee
 * @since 2023/11/05
 */
public class ResultConcurrentMap extends ResultMap<String, LongAdder, Long> {

    public ResultConcurrentMap(Map<String, LongAdder> map) {
        super(map);
    }

    @Override
    BiFunction<String, LongAdder, LongAdder> computeIfAbsentIfPresent(Map.Entry<String, Long> entry) {
        return (k, v) -> {
            if (v == null) {
                v = new LongAdder();
            }
            v.add(entry.getValue());
            return v;
        };
    }

    @Override
    public List<AggregateInsertMapperDto> toList(Long concertSeq) {
        return resultMap.entrySet().stream()
                .map(entry -> AggregateInsertMapperDto.builder()
                        .concertSeq(concertSeq)
                        .gradeName(entry.getKey())
                        .orderTry(entry.getValue().longValue())
                        .build())
                .toList();
    }
}
