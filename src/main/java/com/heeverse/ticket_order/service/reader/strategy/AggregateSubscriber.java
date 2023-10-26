package com.heeverse.ticket_order.service.reader.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateSubscriber {

    private ExecutorService es = Executors.newFixedThreadPool(7);

    public void subscribe(String key, List<SimpleResponse> ticketSeqList, HashMap<String, LongAdder> map) {
        es.execute(() -> {
            LongAdder longAdder = map.get(key);
            longAdder.add(ticketSeqList.size());
            map.put(key, longAdder);
            log.info("consumer {}", map);
        });
    }
}
