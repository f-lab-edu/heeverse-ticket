package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;

import java.util.concurrent.ExecutionException;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.Request;

public interface AggregationReader {
    void doAggregation(AggregationStrategy strategy, Request request) throws ExecutionException, InterruptedException;
}
