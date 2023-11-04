package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author gutenlee
 * @since 2023/11/05
 */
public class ResultHashMap extends ResultMap<String, Long, Long> {
    public ResultHashMap(Map<String, Long> map) {
        super(map);
    }

    @Override
    BiFunction<String, Long, Long> computeIfAbsentIfPresent(Map.Entry<String, Long> entry) {
        return (k, v) -> {
            if (v == null) {
                v = 0L;
            }
            return (v = v + entry.getValue());
        };
    }

    @Override
    public List<AggregateInsertMapperDto> toList(Long concertSeq) {
        return resultMap.entrySet().stream()
                .map(entry -> AggregateInsertMapperDto.builder()
                        .concertSeq(concertSeq)
                        .gradeName(entry.getKey())
                        .orderTry(entry.getValue())
                        .build())
                .toList();
    }
}
