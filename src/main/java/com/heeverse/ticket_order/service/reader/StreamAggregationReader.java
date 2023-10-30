package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StreamAggregationReader implements AggregationReader {

    private final TicketMapper ticketMapper;
    private final StreamAggregationStrategy strategy;

    public void getResultGroupByGrade(AggregateSelectMapperDto.Request request) {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());

        strategy.execute(request.concertSeq(), tickets);

    }
}
