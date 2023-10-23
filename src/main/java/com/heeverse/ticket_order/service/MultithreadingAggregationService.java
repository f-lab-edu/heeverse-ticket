package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.AggregationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class MultithreadingAggregationService implements AggregationService {

    private final BeanFactory beanFactory;

    @Override
    public List<AggregateDto.Response> aggregate(AggregateDto.Request request) {

        try {

            AggregationReader readerBean = getReaderBean(request.getStrategyType());

            List<AggregateSelectMapperDto.Response> responseList
                    = readerBean.getResultGroupByGrade(new AggregateSelectMapperDto.Request(request.getConcertSeq()));

            return responseList.stream()
                    .map(AggregateDto.Response::new)
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("멀티스레딩 집계중 오류 발생", e);
        }

    }

    private AggregationReader getReaderBean(StrategyType strategyType) {
        return beanFactory.getBean(StrategyType.getReaderClazz(strategyType));
    }

}
