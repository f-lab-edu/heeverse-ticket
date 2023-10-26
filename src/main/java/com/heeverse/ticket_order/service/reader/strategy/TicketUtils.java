package com.heeverse.ticket_order.service.reader.strategy;

import com.heeverse.ticket.domain.entity.Ticket;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;

/**
 * @author gutenlee
 * @since 2023/10/26
 */
@UtilityClass
public class TicketUtils {

    public static long minSeq(List<Ticket> tickets) {
        return tickets.stream().min(Comparator.comparing(Ticket::getSeq))
                .orElseThrow(() -> new IllegalArgumentException("최솟값을 구할 수 없습니다"))
                .getSeq();
    }

    public static long maxSeq(List<Ticket> tickets) {
        return tickets.stream().max(Comparator.comparing(Ticket::getSeq))
                .orElseThrow(() -> new IllegalArgumentException("최댓값을 구할 수 없습니다"))
                .getSeq();
    }
}
