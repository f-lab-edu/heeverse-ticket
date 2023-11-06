package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.config.LockScanMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@LockScanMapper
public interface TicketOrderAggregationMapper {
    List<AggregateSelectMapperDto.Response> selectGroupByGradeName(Long concertSeq);
    List<AggregateSelectMapperDto.Response> selectGroupByGradeNameDeNormalization(Long concertSeq);

    void insertAggregationResult(List<AggregateInsertMapperDto> request);
}
