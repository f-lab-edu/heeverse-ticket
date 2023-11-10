package com.heeverse.ticket_order.service.reader.strategy;

public interface AggregationStrategy {
    void execute(AggregationJobWrapper jobWrapper);
}
