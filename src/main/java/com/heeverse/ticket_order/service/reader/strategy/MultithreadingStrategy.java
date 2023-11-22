package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.firstclass.ResultConcurrentMap;
import com.heeverse.ticket_order.service.reader.subscriber.AggregateSubscriber;
import com.heeverse.ticket_order.service.reader.subscriber.TaskMessage;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.heeverse.common.util.MultiThreadUtils.*;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultithreadingStrategy implements AggregationStrategy {

    private final TicketAggrFacade ticketAggrFacade;
    private final ExecutorService ioBoundWorker = Executors.newFixedThreadPool(4);
    private final ExecutorService cpuBoundWorker = Executors.newFixedThreadPool(2);
    private final ResultDBTransfer transfer;


    @Override
    public void execute(AggregationJobWrapper jobWrapper) {

        var concertSeq = jobWrapper.concertSeq();
        GradeInfo gradeInfo = jobWrapper.gradeInfo();
        List<List<Long>> chunks = jobWrapper.chunks();

        AtomicInteger taskCount = new AtomicInteger(chunks.size());

        AggregateSubscriber subscriber = new AggregateSubscriber();
        for (var paging : chunks) {
            CompletableFuture.supplyAsync(() -> ticketAggrFacade.read(paging), ioBoundWorker)
                    .thenApplyAsync(res -> subscriber.subscribe(new TaskMessage<>(concertSeq, res, gradeInfo)), cpuBoundWorker)
                    .thenAcceptAsync(resultMap -> saveIfDone(concertSeq, taskCount, resultMap), cpuBoundWorker);
        }
    }

    private void saveIfDone(long concertSeq, AtomicInteger taskCount, ResultConcurrentMap resultMap) {
        if (taskCount.decrementAndGet() == 0) {
            transfer.transferAll(resultMap.toList(concertSeq));
        }
    }

}
