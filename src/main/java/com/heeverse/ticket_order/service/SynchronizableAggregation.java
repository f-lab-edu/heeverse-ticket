package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;

import java.util.List;

public interface SynchronizableAggregation {
    List<AggregateDto.Response> aggregate(AggregateDto.Request request);

}
