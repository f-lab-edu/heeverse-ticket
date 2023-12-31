package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * K : Map의 Key 타입 <br>
 * V : Map의 Value 타입 <br>
 * E : Map에 {@link #add} 로 추가할 Map.Entry의 Value 타입 <br>
 *
 * @author gutenlee
 * @since 2023/10/28
 */
@Slf4j
public abstract class ResultMap<K, V, E> {

    protected final Map<K, V> resultMap;

    public ResultMap(Map<K, V> map) {
        this.resultMap = map;
    }

    public void add(Map.Entry<K, E> entry) {
        resultMap.compute(entry.getKey(), computeIfAbsentIfPresent(entry));
    }

    abstract BiFunction<K, V, V> computeIfAbsentIfPresent(Map.Entry<K, E> entry);

    public abstract List<AggregateInsertMapperDto> toList(Long concertSeq);

    @Override
    public String toString() {
        return "ResultMap{" +
                "resultMap=" + resultMap +
                '}';
    }
}
