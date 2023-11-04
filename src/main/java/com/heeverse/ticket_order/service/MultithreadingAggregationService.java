package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.CommonAggregationReader;
import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MultithreadingAggregationService implements AsynchronizableAggregation {

    private final BeanFactory beanFactory;
    private final CommonAggregationReader aggregationReader;

    @Override
    public void aggregate(AggregateDto.Request request) {

        aggregationReader.doAggregation(
                getReaderStrategy(request.getStrategyType()),
                new AggregateSelectMapperDto.Request(request.getConcertSeq())
        );

    }

    private AggregationStrategy getReaderStrategy(StrategyType strategyType) {
        return beanFactory.getBean(StrategyType.getReaderClazz(strategyType));
    }

}
