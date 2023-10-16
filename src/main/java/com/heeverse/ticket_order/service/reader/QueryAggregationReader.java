package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
@Component
@RequiredArgsConstructor
public class QueryAggregationReader {

    private final TicketOrderAggregationMapper resultMapper;


    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(
            AggregateSelectMapperDto.Request request
    ) {
        return resultMapper.selectGroupByGradeName(request.concertSeq());
    }


    public List<AggregateSelectMapperDto.Response> getResultDeNormalization(
            AggregateSelectMapperDto.Request request
    ) {

        return resultMapper.selectGroupByGradeNameDeNormalization(request.concertSeq());
    }
}
