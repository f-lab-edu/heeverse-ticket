package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketOrderAggregationMapper {
    List<AggregateSelectMapperDto.Response> selectGroupByGradeName(Long concertSeq);
    List<AggregateSelectMapperDto.Response> selectGroupByGradeNameDeNormalization(Long concertSeq);
    List<AggregateSelectMapperDto.Response> selectByTicketSeqList(List<Long> ticketSeqList);
    void insertAggregationResult(List<AggregateInsertMapperDto> request);
}
