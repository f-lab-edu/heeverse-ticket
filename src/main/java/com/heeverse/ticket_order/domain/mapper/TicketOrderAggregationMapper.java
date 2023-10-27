package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.*;

@Mapper
public interface TicketOrderAggregationMapper {
    List<Response> selectGroupByGradeName(Long concertSeq);
    List<Response> selectGroupByGradeNameDeNormalization(Long concertSeq);
    List<SimpleResponse> selectByTicketSeqList(List<Long> ticketSeqList);
    List<SimpleResponse> selectByTicketSeqBetween(ZeroOffsetRequest zeroOffsetRequest);
    MinMaxResponse selectMinMax(ZeroOffsetRequest zeroOffsetRequest);
    void insertAggregationResult(List<AggregateInsertMapperDto> request);
}
