package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.*;
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
    private final AggregateSubscriber subscriber;
    private final ExecutorService es = Executors.newFixedThreadPool(3);
    private final static int SIZE = 2_000_000;
    @Override
    public List<Response> execute(long concertSeq, List<Ticket> tickets) {

        Map<String, List<Ticket>> groupBy = tickets.stream().collect((groupingBy(Ticket::getGradeName)));

        CountDownLatch latch = new CountDownLatch(groupBy.size());

        HashMap<String, LongAdder> map = new HashMap<>();

        for (Map.Entry<String, List<Ticket>> entry : groupBy.entrySet()) {
            es.execute(() -> {

                long fromTicketSeq = TicketUtils.minSeq(entry.getValue());
                long toTicketSeq = TicketUtils.maxSeq(entry.getValue());
                long offset = aggregationMapper.selectByTicketSeqBetween(
                        ZeroOffsetRequest.start(concertSeq, fromTicketSeq, toTicketSeq)).get(0).seq();

                map.put(entry.getKey(), new LongAdder());
                while (true) {
                    log.info("grade {} / offset {} ", entry.getKey(), offset);
                    List<SimpleResponse> responseList
                            = aggregationMapper.selectByTicketSeqBetween(
                                    new ZeroOffsetRequest(concertSeq,
                                            fromTicketSeq,
                                            toTicketSeq,
                                            offset,
                                            SIZE
                                    ));
                    if (CollectionUtils.isEmpty(responseList)) {
                        break;
                    }
                    subscriber.subscribe(entry.getKey(), responseList, map);

                    offset = responseList.get(responseList.size() - 1).seq();
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("집계 결과 {}", map);
        return map.entrySet()
                .stream()
                .map(entry -> new Response(
                        concertSeq, entry.getKey(), groupBy.get(entry.getKey()).size(), entry.getValue().intValue())
                ).toList();
    }
}
