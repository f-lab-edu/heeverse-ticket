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


    private final Class<? extends AggregationStrategy> strategyClazz;

    StrategyType(Class<? extends AggregationStrategy> strategyClazz) {
        this.strategyClazz = strategyClazz;
    }

    public static Class<? extends AggregationStrategy> getStrategyClass(StrategyType strategyType) {
        return switch (strategyType) {
            case SINGLE_THREAD -> SINGLE_THREAD.strategyClazz;
            case MULTI_THREAD -> MULTI_THREAD.strategyClazz;
            case STREAM -> STREAM.strategyClazz;
            case QUERY -> throw new IllegalArgumentException("잘못된 타입 " + strategyType);
        };
    }
}
