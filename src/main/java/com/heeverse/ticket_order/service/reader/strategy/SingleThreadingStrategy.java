package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SingleThreadingStrategy implements AggregationStrategy {

    private final TicketOrderAggregationMapper aggregationMapper;

    @Override
    public void execute(ExecutorService es, List<Ticket> ticketList) {

        Map<String, List<Ticket>> collected = ticketList.stream()
                .collect(groupingBy(Ticket::getGradeName));


        for (Map.Entry<String, List<Ticket>> entry : collected.entrySet()) {
            List<Ticket> list = entry.getValue();
            List<AggregateSelectMapperDto.Response> responseList
                    = aggregationMapper.selectByTicketSeqList(list.stream().map(Ticket::getSeq).collect(Collectors.toList()));
            log.info("{} orderTry {}", entry.getKey(), responseList.size());
        }
    }
}
