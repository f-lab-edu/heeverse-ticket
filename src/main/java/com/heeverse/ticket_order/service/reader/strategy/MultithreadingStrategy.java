package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultithreadingStrategy implements AggregationStrategy {

    private final TicketOrderAggregationMapper aggregationMapper;
    @Override
    public List<AggregateSelectMapperDto.Response> execute(ExecutorService es, List<Ticket> tickets) {

        Map<String, List<Ticket>> collected
                = tickets.stream()
                    .collect(groupingBy(Ticket::getGradeName));

        ConcurrentLinkedQueue<AggregateSelectMapperDto.Response> queue = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(1);

        for (Map.Entry<String, List<Ticket>> entry : collected.entrySet()) {
            es.execute(() -> {
                List<Long> seqList = entry.getValue().stream().map(Ticket::getSeq).collect(Collectors.toList());
                List<AggregateSelectMapperDto.Response> responses = aggregationMapper.selectByTicketSeqList(seqList);
                queue.addAll(responses);
                log.info("{} : order Try {}", entry.getKey(), responses.size());
            });
        }

        ConcurrentHashMap<String, AtomicLong> result = new ConcurrentHashMap<>();

        while (!queue.isEmpty()) {
            es.execute(() -> {
                AggregateSelectMapperDto.Response poll = queue.poll();
                log.info("poll = " + poll);
                assert poll != null;
                result.putIfAbsent(poll.gradeName(), new AtomicLong(1));
                result.computeIfPresent(poll.gradeName(), (a, b) -> {
                    b.incrementAndGet();
                    return b;
                });
            });
            if (queue.isEmpty())
                latch.countDown();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
