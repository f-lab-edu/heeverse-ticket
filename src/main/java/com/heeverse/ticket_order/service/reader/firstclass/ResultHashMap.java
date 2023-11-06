package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author gutenlee
 * @since 2023/11/05
 */
public class ResultHashMap extends ResultMap<String, Long, Long> {
    public ResultHashMap() {
        super(new HashMap<>());
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
        return toList(getHashMap(), concertSeq);
    }

    private Map<String, Long> getHashMap() {
        return this.resultMap;
    }


    public static List<AggregateInsertMapperDto> toList(Map<String, Long> map, long concertSeq) {
        return map.entrySet().stream()
                .map(entry -> AggregateInsertMapperDto.builder()
                        .concertSeq(concertSeq)
                        .gradeName(entry.getKey())
                        .orderTry(entry.getValue())
                        .build())
                .toList();
    }
}
