package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket_order.service.reader.StreamHelper;
import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.firstclass.ResultHashMap;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public void execute(AggregationJobWrapper jobWrapper){

        GradeInfo gradeInfo = jobWrapper.gradeInfo();
        List<List<Long>> chunks = jobWrapper.chunks();

        Map<String, Long> result = getTicketOrderLog(chunks)
                .map(list -> getGroupByGrade(gradeInfo, list))
                .flatMap(StreamHelper::toEntrySetStream)
                .collect(Collectors.groupingByConcurrent(
                        Map.Entry::getKey,
                        Collectors.summingLong(Map.Entry::getValue)
                ));

        resultDBTransfer.transferAll(ResultHashMap.toList(result, jobWrapper.concertSeq()));
    }


    private Stream<List<AggregateSelectMapperDto.SimpleResponse>> getTicketOrderLog(List<List<Long>> chunks) {
        return new CopyOnWriteArrayList<>(chunks)
                .parallelStream()
                .map(ticketAggrFacade::read);
    }

    private Map<String, Long> getGroupByGrade(
            GradeInfo gradeInfo,
            List<AggregateSelectMapperDto.SimpleResponse> list
    ) {
        return list.stream().collect(StreamHelper.countGroupingByKey(gradeInfo));
    }
}
