package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public class SingleAggregationReader implements AggregationReader{

    private final TicketMapper ticketMapper;
    private final TicketOrderAggregationMapper aggregationMapper;

    @Override
    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateSelectMapperDto.Request request) {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());

        Map<String, List<Ticket>> collected = tickets.stream()
                .collect(groupingBy(Ticket::getGradeName));


        ArrayList<AggregateSelectMapperDto.Response> result = new ArrayList<>();

        for (Map.Entry<String, List<Ticket>> entry : collected.entrySet()) {
            List<Ticket> list = entry.getValue();
            List<AggregateSelectMapperDto.SimpleResponse> responseList
                    = aggregationMapper.selectByTicketSeqList(list.stream().map(Ticket::getSeq).collect(Collectors.toList()));

            result.add(new AggregateSelectMapperDto.Response(request.concertSeq(), entry.getKey(), entry.getValue().size(), responseList.size()));
        }

        return result;
    }
}
