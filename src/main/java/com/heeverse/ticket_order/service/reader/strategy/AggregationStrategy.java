package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AggregationStrategy {
    void execute(long concertSeq, List<Ticket> ticketList);
}
