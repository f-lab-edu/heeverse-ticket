package com.heeverse.ticket_order.service.reader;

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
public class FirstMultithreadingStrategy implements MultithreadingStrategy {

    private final TicketOrderAggregationMapper aggregationMapper;
    @Override
    public void execute(ExecutorService es, List<Ticket> tickets) {

        Map<String, List<Ticket>> collected
                = tickets.stream()
                    .collect(groupingBy(Ticket::getGradeName));

        collected.entrySet()
                .parallelStream()
                .forEach(entry -> {
                    List<Long> seqList = entry.getValue().stream().map(Ticket::getSeq).collect(Collectors.toList());
                    List<AggregateSelectMapperDto.Response> responses = aggregationMapper.selectByTicketSeqList(seqList);
                    log.info("order Try {}", responses.size());
                });
    }
}
