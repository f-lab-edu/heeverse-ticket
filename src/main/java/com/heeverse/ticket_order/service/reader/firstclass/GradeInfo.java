package com.heeverse.ticket_order.service.reader.firstclass;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.common.util.TicketUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author gutenlee
 * @since 2023/10/27
 */

/**
 * TicketSeq 가공을 위한 일급 컬렉션
 */
public class GradeInfo {

    private final Map<String, MinMax> groupBy;
    public GradeInfo(List<Ticket> tickets) {
        Map<String, List<Ticket>> map = tickets.stream().collect((groupingBy(Ticket::getGradeName)));
        this.groupBy = initMinMax(map);
    }

    public String getGrade(long ticketSeq){
        return groupBy.entrySet().stream()
                .filter(entry -> {
                    MinMax minMax = entry.getValue();
                    return minMax.isWithinRange(ticketSeq);
                })
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("티켓 시퀀스에 속하지 않는 시퀀스입니다 : " + ticketSeq));
    }


    private Map<String, MinMax> initMinMax(Map<String, List<Ticket>> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> getMinMax().apply(entry)));
    }


    private Function<Map.Entry<String, List<Ticket>>, MinMax> getMinMax() {
        return entry -> new MinMax(TicketUtils.minSeq(entry.getValue()), TicketUtils.maxSeq(entry.getValue()), entry.getValue().size());
    }


    public record MinMax (
            long start,
            long end,
            long totalCount
    ) {
        public boolean isWithinRange(long ticketSeq) {
            return start <= ticketSeq && ticketSeq <= end;
        }
    }
}
