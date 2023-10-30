package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;

public interface AsynchronizableAggregation {
    void aggregate(AggregateDto.Request request);

}
