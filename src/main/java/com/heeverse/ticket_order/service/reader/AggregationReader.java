package com.heeverse.ticket_order.service.reader;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.Request;
import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.Response;

public interface AggregationReader {
    List<Response> getResultGroupByGrade(Request request) throws ExecutionException, InterruptedException;
}
