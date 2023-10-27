package com.heeverse.ticket_order.domain.dto.persistence;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import lombok.Getter;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
@Getter
public class AggregateInsertMapperDto {

    private final long concertSeq;
    private final String gradeName;
    private final int totalTickets;
    private final int orderTry;

    public AggregateInsertMapperDto(long concertSeq, String gradeName, int totalTickets, long orderTry) {
        this.concertSeq = concertSeq;
        this.gradeName = gradeName;
        this.totalTickets = totalTickets;
        this.orderTry = (int) orderTry;
    }

    public AggregateInsertMapperDto(AggregateDto.Response dto) {
        this(dto.getConcertSeq(), dto.getGradeName(), dto.getTotalTickets(), dto.getOrderTry());
    }

}
