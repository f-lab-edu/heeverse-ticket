package com.heeverse.ticket_order.service.transfer;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
@Component
public class ResultDBTransfer implements ResultTransfer<AggregateInsertMapperDto> {

    private final TicketOrderAggregationMapper aggregationMapper;

    public ResultDBTransfer(TicketOrderAggregationMapper aggregationMapper) {
        this.aggregationMapper = aggregationMapper;
    }

    @Override
    public List<AggregateInsertMapperDto> transferAll(List<AggregateInsertMapperDto> collections) {
        aggregationMapper.insertAggregationResult(collections);
        return collections;
    }

}
