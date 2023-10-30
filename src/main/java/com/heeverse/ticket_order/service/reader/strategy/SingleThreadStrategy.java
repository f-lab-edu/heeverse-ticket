package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.TicketAggrFacade;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final static int CHUNK_SIZE = 100;

    @Override
    public void execute(long concertSeq, List<Ticket> ticketList) {

        GradeInfo gradeInfo = new GradeInfo(ticketList);
        List<Long> collectedTicketSeq = TicketUtils.collectTicketSeq(ticketList);

        List<List<Long>> chunks = PaginationProvider.toChunk(collectedTicketSeq, CHUNK_SIZE);
        log.info("chunk size {}", chunks.size());

        final HashMap<String, Long> resultMap = new HashMap<>();
        for (List<Long> chunk : chunks) {
            List<AggregateSelectMapperDto.SimpleResponse> simpleResponses = ticketAggrFacade.read(chunk);
            Map<String, Long> map = simpleResponses.stream().collect(Collectors.groupingBy(res -> gradeInfo.getGrade(res.ticketSeq()), Collectors.counting()));
            reduceTask(resultMap, map);
        }

        resultDBTransfer.transferAll(getAggregateInsertMapperDtoList(concertSeq, resultMap));
    }

    private void reduceTask(HashMap<String, Long> resultMap, Map<String, Long> map) {
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (!resultMap.containsKey(key)) {
                resultMap.put(key, value);
            } else {
                Long v1 = resultMap.get(key);
                resultMap.put(key, value = (v1 + value));
            }
        }
    }

    private static List<AggregateInsertMapperDto> getAggregateInsertMapperDtoList(
            long concertSeq,
            HashMap<String, Long> resultMap
    ) {
        return resultMap.entrySet().stream()
                .map(entry -> new AggregateInsertMapperDto(concertSeq, entry.getKey(), entry.getValue()))
                .toList();
    }
}
