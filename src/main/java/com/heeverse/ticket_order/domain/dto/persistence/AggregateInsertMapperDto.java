package com.heeverse.ticket_order.domain.dto.persistence;

import com.heeverse.common.util.PrimitiveUtils;
import com.heeverse.ticket_order.domain.dto.AggregateDto;
import lombok.Builder;
import lombok.Getter;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
@Getter
public class AggregateInsertMapperDto {

    private final long concertSeq;
    private final String gradeName;
    private final int orderTry;

    @Builder
    public AggregateInsertMapperDto(long concertSeq, String gradeName, long orderTry) {
        this.concertSeq = concertSeq;
        this.gradeName = gradeName;
        this.orderTry = PrimitiveUtils.toIntSafely(orderTry);
    }

    public AggregateInsertMapperDto(AggregateDto.Response dto) {
        this(dto.getConcertSeq(), dto.getGradeName(), dto.getOrderTry());
    }

}
