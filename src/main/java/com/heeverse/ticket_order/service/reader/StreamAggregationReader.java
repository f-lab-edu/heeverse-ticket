package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StreamAggregationReader {

    private final TicketMapper ticketMapper;
    private final TicketOrderAggregationMapper aggregationMapper;

    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateSelectMapperDto.Request request) {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());

        Map<String, List<Ticket>> collected
                = tickets.stream()
                .collect(groupingBy(Ticket::getGradeName));


        return collected.entrySet().parallelStream()
                .map(entry -> {
                    List<Long> seqList = entry.getValue().stream().map(Ticket::getSeq).collect(Collectors.toList());
                    List<AggregateSelectMapperDto.Response> responses = aggregationMapper.selectByTicketSeqList(seqList);
                    log.info("{} : order Try {}", entry.getKey(), responses.size());
                    return new AggregateSelectMapperDto.Response(request.concertSeq(), entry.getKey(), seqList.size(), responses.size());
                }).collect(Collectors.toList());
    }
}
