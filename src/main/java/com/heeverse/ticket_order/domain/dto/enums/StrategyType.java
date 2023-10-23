package com.heeverse.ticket_order.domain.dto.enums;

import com.heeverse.ticket_order.service.reader.AggregationReader;
import com.heeverse.ticket_order.service.reader.MultiAggregationReader;
import com.heeverse.ticket_order.service.reader.SingleAggregationReader;
import com.heeverse.ticket_order.service.reader.StreamAggregationReader;

public enum StrategyType{

    SINGLE_THREAD(SingleAggregationReader.class),
    MULTI_THREAD(MultiAggregationReader.class),
    STREAM(StreamAggregationReader.class),
    QUERY(null)
    ;


    private final Class<? extends AggregationReader> reader;

    StrategyType(Class<? extends AggregationReader> reader) {
        this.reader = reader;
    }

    public static Class<? extends AggregationReader> getReaderClazz(StrategyType strategyType) {
        return switch (strategyType) {
            case SINGLE_THREAD -> SINGLE_THREAD.reader;
            case MULTI_THREAD -> MULTI_THREAD.reader;
            case STREAM -> STREAM.reader;
            case QUERY -> throw new IllegalArgumentException("잘못된 타입 " + strategyType);
        };
    }
}
