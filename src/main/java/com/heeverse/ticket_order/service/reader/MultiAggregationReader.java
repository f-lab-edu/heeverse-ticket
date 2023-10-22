package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@Slf4j
public class MultiAggregationReader {

    private final TicketMapper ticketMapper;
    private final ExecutorService es;

    public MultiAggregationReader(TicketMapper ticketMapper, ExecutorService fixedPool) {
        this.ticketMapper = ticketMapper;
        this.es = fixedPool;
    }

    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(
            AggregateSelectMapperDto.Request request,
            AggregationStrategy strategy
    )
            throws ExecutionException, InterruptedException {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());
        strategy.execute(es, tickets);
        return null;
    }
}
