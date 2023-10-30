package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.AggregationReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MultithreadingAggregationService implements AsynchronizableAggregation {

    private final BeanFactory beanFactory;

    @Override
    public void aggregate(AggregateDto.Request request) {

        try {
            AggregationReader readerBean = getReaderBean(request.getStrategyType());
            readerBean.getResultGroupByGrade(new AggregateSelectMapperDto.Request(request.getConcertSeq()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("멀티스레딩 집계중 오류 발생", e);
        }

    }

    private AggregationReader getReaderBean(StrategyType strategyType) {
        return beanFactory.getBean(StrategyType.getReaderClazz(strategyType));
    }

}
