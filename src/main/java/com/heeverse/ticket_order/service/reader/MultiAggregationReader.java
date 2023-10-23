package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@Slf4j
public class MultiAggregationReader implements AggregationReader {

    private final TicketMapper ticketMapper;
    private final ExecutorService es;
    private final MultithreadingStrategy strategy;

    public MultiAggregationReader(TicketMapper ticketMapper,
                                  ExecutorService fixedPool,
                                  MultithreadingStrategy strategy
    ) {
        this.ticketMapper = ticketMapper;
        this.es = fixedPool;
        this.strategy = strategy;
    }

    @Override
    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateSelectMapperDto.Request request) {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());
        return strategy.execute(es, tickets);
    }
}
