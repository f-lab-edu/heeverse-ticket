package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;

public interface AsynchronizableAggregation {
    void aggregate(AggregateSelectMapperDto.Request request);

}
