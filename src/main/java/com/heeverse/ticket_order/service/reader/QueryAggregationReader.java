package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
@Component
public class QueryAggregationReader {

    private final TicketOrderAggregationMapper resultMapper;

    public QueryAggregationReader(TicketOrderAggregationMapper resultMapper) {
        this.resultMapper = resultMapper;
    }


    public List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateSelectMapperDto.Request request) {
        return resultMapper.selectGroupByGradeName(request.concertSeq());
    }
}
