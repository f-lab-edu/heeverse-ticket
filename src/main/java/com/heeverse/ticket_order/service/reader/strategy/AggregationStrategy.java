package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public interface AggregationStrategy {
    void execute(ExecutorService es, List<Ticket> ticketList) throws ExecutionException, InterruptedException;
}
