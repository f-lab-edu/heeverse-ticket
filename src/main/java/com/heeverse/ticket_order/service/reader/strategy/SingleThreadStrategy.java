package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.StreamHelper;
import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.firstclass.ResultHashMap;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.*;

/**
 * @author gutenlee
 * @since 2023/10/30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SingleThreadStrategy implements AggregationStrategy {

    private final TicketAggrFacade ticketAggrFacade;
    private final ResultDBTransfer resultDBTransfer;

    @Override
    public void execute(AggregationJobWrapper jobWrapper) {

        ResultHashMap resultHashMap = new ResultHashMap(new HashMap<>());

        for (var chunk : jobWrapper.chunks()) {
            reduceTask(resultHashMap, jobWrapper.gradeInfo(), ticketAggrFacade.read(chunk));
        }

        resultDBTransfer.transferAll(resultHashMap.toList(jobWrapper.concertSeq()));
    }

    private void reduceTask(
            ResultHashMap resultMap,
            GradeInfo gradeInfo,
            List<SimpleResponse> simpleResponses
    ) {
        Map<String, Long> map = simpleResponses.stream().collect(StreamHelper.countGroupingByKey(gradeInfo));
        for (var entry : map.entrySet()) {
            resultMap.add(entry);
        }
    }

}
