package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.reader.QueryAggregationReader;
import com.heeverse.ticket_order.service.transfer.ResultTransfer;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class QueryAggregationService implements AggregationService {

    private final QueryAggregationReader reader;
    private final ResultTransfer<AggregateInsertMapperDto> transfer;

    @Transactional(readOnly = true)
    @Override
    public List<AggregateDto.Response> aggregate(AggregateDto.Request aggrDto) {

        List<AggregateSelectMapperDto.Response> responseList
                = getResultGroupByGrade(aggrDto);

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

    private List<AggregateSelectMapperDto.Response> getResultGroupByGrade(AggregateDto.Request aggrDto) {
        if (aggrDto.isNormalization()) {
            return reader.getResultGroupByGrade(new AggregateSelectMapperDto.Request(aggrDto.getConcertSeq()));
        }
        return reader.getResultDeNormalization(new AggregateSelectMapperDto.Request(aggrDto.getConcertSeq()));
    }
}
