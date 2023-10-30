package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.firstclass.ResultMap;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gutenlee
 * @since 2023/10/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StreamAggregationStrategy implements AggregationStrategy {

    private final TicketAggrFacade ticketAggrFacade;
    private final ResultDBTransfer resultDBTransfer;
    private final static int CHUNK_SIZE = 100;

    @Override
    public void execute(long concertSeq, List<Ticket> ticketList){
        GradeInfo gradeInfo = new GradeInfo(ticketList);
        List<Long> collectedTicketSeq = TicketUtils.collectTicketSeq(ticketList);

        List<List<Long>> chunks = PaginationProvider.toChunk(collectedTicketSeq, CHUNK_SIZE);
        log.info("chunk size {}", chunks.size());

        ResultMap resultMap = new ResultMap();
        Collections.synchronizedList(chunks)
                .parallelStream()
                .map(ticketAggrFacade::read)
                .flatMap(list -> {
                    Map<String, Long> map = list.stream().collect(Collectors.groupingBy(res -> gradeInfo.getGrade(res.ticketSeq()), Collectors.counting()));
                    return map.entrySet().stream();
                })
                .forEach(resultMap::add);

        resultDBTransfer.transferAll(resultMap.toList(concertSeq));
    }
}
