package com.heeverse.ticket_order.domain.dto.enums;

import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadStrategy;
import com.heeverse.ticket_order.service.reader.strategy.StreamAggregationStrategy;

public enum StrategyType{

    SINGLE_THREAD(SingleThreadStrategy.class),
    MULTI_THREAD(MultithreadingStrategy.class),
    STREAM(StreamAggregationStrategy.class),
    QUERY(null)
    ;


    private final Class<? extends AggregationStrategy> reader;

    StrategyType(Class<? extends AggregationStrategy> reader) {
        this.reader = reader;
    }

    public static Class<? extends AggregationStrategy> getReaderClazz(StrategyType strategyType) {
        return switch (strategyType) {
            case SINGLE_THREAD -> SINGLE_THREAD.reader;
            case MULTI_THREAD -> MULTI_THREAD.reader;
            case STREAM -> STREAM.reader;
            case QUERY -> throw new IllegalArgumentException("잘못된 타입 " + strategyType);
        };
    }
}
