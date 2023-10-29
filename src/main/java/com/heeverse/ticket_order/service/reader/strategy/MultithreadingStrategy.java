package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.producer.TaskMessage;
import com.heeverse.ticket_order.service.reader.producer.TaskPublisher;
import com.heeverse.ticket_order.service.reader.subscriber.AggregateSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heeverse.common.util.MultiThreadUtils.availableCores;
import static com.heeverse.common.util.MultiThreadUtils.calculateTaskSizePerCore;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultithreadingStrategy implements AggregationStrategy {

    private final TicketAggrFacade ticketAggrFacade;
    private final TaskPublisher publisher;
    private final ExecutorService ioBoundWorker = Executors.newFixedThreadPool(availableCores() / 2);
    private final ExecutorService cpuBoundWorker = Executors.newFixedThreadPool(availableCores());

    private final static int CHUNK_SIZE = 100;

    @Override
    public void execute(long concertSeq, List<Ticket> tickets) {

        GradeInfo gradeInfo = new GradeInfo(tickets);
        List<Long> collectedTicketSeq = TicketUtils.collectTicketSeq(tickets);

        List<List<Long>> chunk = PaginationProvider.toChunk(collectedTicketSeq, CHUNK_SIZE);
        log.info("chunk size {}", chunk.size());

        AggregateSubscriber subscriber = new AggregateSubscriber();
        for (var paging : chunk) {
            String uuid = publisher.generateUuid();
            CompletableFuture.supplyAsync(() -> ticketAggrFacade.read(paging), ioBoundWorker)
                    .thenAcceptAsync(res -> publisher.publish(
                            new TaskMessage<>(uuid, concertSeq, chunk.size(), res, gradeInfo), subscriber), cpuBoundWorker);
        }
    }

}
