package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.service.reader.producer.TaskMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

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
        resultMap.computeIfAbsent(entry.getKey(), r -> getInit());
        resultMap.computeIfPresent(entry.getKey(), (key, a) -> {
            a.add(entry.getValue());
            return a;
        });
    }

    public List<AggregateInsertMapperDto> toList(TaskMessage<List<SimpleResponse>> taskMessage) {
        return resultMap.entrySet().stream()
                .map(entry -> new AggregateInsertMapperDto(taskMessage.concertSeq(), entry.getKey(), 0, entry.getValue().longValue()))
                .toList();
    }

    private LongAdder getInit() {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        return longAdder;
    }
}
