package com.heeverse.ticket_order.domain.dto.persistence;

import com.heeverse.ticket_order.domain.dto.AggregateDto;
import com.heeverse.ticket_order.domain.dto.StrategyDto;
import jakarta.validation.constraints.Min;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
public class AggregateSelectMapperDto {

    public record Request (
            @Min(0)
            long concertSeq,
            StrategyDto strategyDto
    ) {

        public static Request from(AggregateDto.Request request) {

            if (request.isQuery()) {
                return new Request(request.getConcertSeq(), null);
            }

            return new Request(
                    request.getConcertSeq(),
                    new StrategyDto(request.getStrategyType(), request.getPageSize())
            );
        }
    }



    public record Response (
            Long concertSeq,
            String gradeName,
            Integer totalTickets,
            Integer orderTry
    ) { }


    public record SimpleResponse (
            long seq,
            long ticketSeq
    ) { }

}
