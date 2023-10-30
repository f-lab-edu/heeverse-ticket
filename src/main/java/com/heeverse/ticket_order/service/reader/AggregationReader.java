package com.heeverse.ticket_order.service.reader;

import java.util.concurrent.ExecutionException;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.Request;

public interface AggregationReader {
    void getResultGroupByGrade(Request request) throws ExecutionException, InterruptedException;
}
