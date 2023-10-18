package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.reader.FirstMultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.MultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.NonMultithreadingStrategy;
import com.heeverse.ticket_order.service.reader.SimpleAggregationReader;
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

    private final SimpleAggregationReader reader;
    private final TicketOrderAggregationMapper aggregationMapper;

    @Override
    public List<AggregateDto.Response> aggregate(AggregateDto.Request request) {
        // ticket

        reader.setStrategy(getStrategy(request.isMultithreading()));

        try {
            List<AggregateSelectMapperDto.Response> responseList
                    = reader.getResultGroupByGrade(new AggregateSelectMapperDto.Request(request.getConcertSeq()));

            return responseList.stream()
                    .map(AggregateDto.Response::new)
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("멀티스레딩 집계중 오류 발생", e);
        }

    }

    private MultithreadingStrategy getStrategy(boolean multithreading) {
        if (multithreading){
            return new FirstMultithreadingStrategy(aggregationMapper);
        }
        return new NonMultithreadingStrategy(aggregationMapper);
    }
}
