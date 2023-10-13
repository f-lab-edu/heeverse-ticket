package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.QueryAggregationReader;
import com.heeverse.ticket_order.service.transfer.ResultTransfer;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
@Service
public class QueryAggregationService implements AggregationService {

    private final QueryAggregationReader reader;
    private final ResultTransfer<AggregateInsertMapperDto> transfer;

    public QueryAggregationService(
            QueryAggregationReader reader,
            ResultDBTransfer transfer
    ) {
        this.reader = reader;
        this.transfer = transfer;
    }


    @Transactional(readOnly = true)
    @Override
    public List<AggregateDto.Response> aggregate(AggregateDto.Request aggrDto) {

        List<AggregateSelectMapperDto.Response> responseList
                = reader.getResultGroupByGrade(new AggregateSelectMapperDto.Request(aggrDto.getConcertSeq()));

        return responseList.stream()
                .map(AggregateDto.Response::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AggregateDto.Response> transfer(List<AggregateDto.Response> responses) {

        Stream<AggregateInsertMapperDto> mapStream = responses.stream().map(AggregateInsertMapperDto::new);
        transfer.transferAll(mapStream.collect(Collectors.toList()));

        return responses;
    }
}
