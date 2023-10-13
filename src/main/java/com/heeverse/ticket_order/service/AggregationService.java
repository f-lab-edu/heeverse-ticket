package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;

import java.util.List;

public interface AggregationService {
    List<AggregateDto.Response> aggregate(AggregateDto.Request request);

}
