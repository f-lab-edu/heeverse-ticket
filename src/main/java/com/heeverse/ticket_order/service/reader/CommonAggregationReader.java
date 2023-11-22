package com.heeverse.ticket_order.service.reader;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.common.util.TicketUtils;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.strategy.AggregationJobWrapper;
import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;
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
public class CommonAggregationReader implements AggregationReader {

    private final TicketMapper ticketMapper;

    @Override
    public void doAggregation(AggregationStrategy strategy, AggregateSelectMapperDto.Request request) {

        var concertSeq = request.concertSeq();
        List<Ticket> ticketList = ticketMapper.findTickets(concertSeq);

        AggregationJobWrapper jobWrapper = new AggregationJobWrapper(
                concertSeq,
                ticketList,
                PaginationProvider.toChunk(TicketUtils.collectTicketSeq(ticketList), request.strategyDto().pageSize()),
                new GradeInfo(ticketList)
        );

        strategy.execute(jobWrapper);

    }
}
