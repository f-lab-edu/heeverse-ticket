package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.service.reader.producer.TaskMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/28
 */
public class ResultMap {

    private final ConcurrentHashMap<String, LongAdder> resultMap;


    public ResultMap() {
        this.resultMap = new ConcurrentHashMap<>();
    }

    public void add(Map.Entry<String, Long> entry) {
        resultMap.compute(entry.getKey(), computeIfAbsentIfPresent(entry));
    }


    public List<AggregateInsertMapperDto> toList(TaskMessage<List<SimpleResponse>> taskMessage) {
        return toEntryStream()
                .map(entry -> AggregateInsertMapperDto.builder()
                                .concertSeq(taskMessage.concertSeq())
                                .gradeName(entry.getKey())
                                .orderTry(entry.getValue().longValue())
                                .build())
                .toList();
    }

    private BiFunction<String, LongAdder, LongAdder> computeIfAbsentIfPresent(Map.Entry<String, Long> entry) {
        return (k, v) -> {
            if (v == null) {
                return getInit();
            } else {
                v.add(entry.getValue());
                return v;
            }
        };
    }

    private LongAdder getInit() {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        return longAdder;
    }

    private Stream<Map.Entry<String, LongAdder>> toEntryStream() {
        return resultMap.entrySet().stream();
    }
}
