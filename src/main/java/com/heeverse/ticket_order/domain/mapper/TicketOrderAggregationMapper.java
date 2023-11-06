package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.config.LockScanMapper;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.*;

@Mapper
@LockScanMapper
public interface TicketOrderAggregationMapper {
    List<Response> selectGroupByGradeName(Long concertSeq);
    List<Response> selectGroupByGradeNameDeNormalization(Long concertSeq);
    List<SimpleResponse> selectTicketSeqWhereIn(List<Long> ticketSeqList);
    void insertAggregationResult(List<AggregateInsertMapperDto> request);
}
