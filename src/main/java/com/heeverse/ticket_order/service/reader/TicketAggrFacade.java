package com.heeverse.ticket_order.service.reader;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/30
 */
@Component
@RequiredArgsConstructor
public class TicketAggrFacade {

    private final TicketOrderAggregationMapper aggregationMapper;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<AggregateSelectMapperDto.SimpleResponse> read(List<Long> list) {
        return aggregationMapper.selectTicketSeqWhereIn(list);
    }
}
