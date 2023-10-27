package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.producer.TaskMessage;
import com.heeverse.ticket_order.service.reader.producer.TaskPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.MinMaxResponse;
import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.ZeroOffsetRequest;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultithreadingStrategy implements AggregationStrategy {

    private final TicketOrderAggregationMapper aggregationMapper;
    private final TaskPublisher publisher;
    private final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final static int SIZE = 50_000;

    @Override
    public void execute(long concertSeq, List<Ticket> tickets) {

        GradeInfo gradeInfo = new GradeInfo(tickets);
        long start = TicketUtils.minSeq(tickets);
        long to = TicketUtils.maxSeq(tickets);

        MinMaxResponse minMax = aggregationMapper.selectMinMax(ZeroOffsetRequest.start(concertSeq, start, to));

        List<Long> offsets = PaginationProvider.getOffset(minMax.minSeq(), minMax.maxSeq(), SIZE);
        log.info("offset size {}", offsets);

        for (long offset : offsets) {
            String uuid = publisher.generateUuid();
            CompletableFuture.supplyAsync(() -> {
                return aggregationMapper.selectByTicketSeqBetween(new ZeroOffsetRequest(concertSeq, start, to, offset, SIZE));
            }, es).thenAcceptAsync(res -> publisher.publish(new TaskMessage<>(uuid, concertSeq, offsets.size(), res, gradeInfo)), es);
        }
    }

}
