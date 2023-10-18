package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.heeverse.common.ExecutorUtils.createFixedThreadPool;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Component
@Slf4j
public class SimpleAggregationReader {

    private final TicketMapper ticketMapper;
    private MultithreadingStrategy strategy;
    private ExecutorService es;

    public SimpleAggregationReader(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
        this.es = createFixedThreadPool(3);
    }

    public void setStrategy(MultithreadingStrategy strategy) {
        this.strategy = strategy;
    }


    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateSelectMapperDto.Request request)
            throws ExecutionException, InterruptedException {

        List<Ticket> tickets = ticketMapper.findTickets(request.concertSeq());
        strategy.execute(es, tickets);
        return null;
    }
}
