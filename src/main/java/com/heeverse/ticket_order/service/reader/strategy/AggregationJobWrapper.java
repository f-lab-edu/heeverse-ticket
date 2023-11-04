package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/11/05
 */
public record AggregationJobWrapper(
        long concertSeq,
        List<Ticket> tickets,
        List<List<Long>> chunks,
        GradeInfo gradeInfo
) {
}
