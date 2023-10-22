package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.reader.strategy.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.strategy.AggregationStrategy;
import com.heeverse.ticket_order.service.reader.strategy.SingleThreadingStrategy;
import com.heeverse.ticket_order.service.reader.MultiAggregationReader;
import lombok.RequiredArgsConstructor;
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

    private final MultiAggregationReader reader;
    private final TicketOrderAggregationMapper aggregationMapper;

    @Override
    public List<AggregateDto.Response> aggregate(AggregateDto.Request request) {
        // ticket

        try {
            List<AggregateSelectMapperDto.Response> responseList
                    = reader.getResultGroupByGrade(
                            new AggregateSelectMapperDto.Request(request.getConcertSeq()),
                            getStrategy(request.isMultithreading())
                        );

            return responseList.stream()
                    .map(AggregateDto.Response::new)
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("멀티스레딩 집계중 오류 발생", e);
        }

    }

    private AggregationStrategy getStrategy(boolean multithreading) {
        if (multithreading){
            return new MultithreadingStrategy(aggregationMapper);
        }
        return new SingleThreadingStrategy(aggregationMapper);
    }
}
