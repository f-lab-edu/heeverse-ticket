package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import lombok.Getter;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
public class AggregateDto {


    public static class Request{
        @Getter
        private final Long concertSeq;

        public Request(Long concertSeq) {
            this.concertSeq = concertSeq;
        }
    }


    @Getter
    public static class Response {

        private final Long concertSeq;
        private final Long ticketSeq;
        private final String gradeName;
        private final Integer totalTickets;
        private final Integer orderTry;

        public Response(
                AggregateSelectMapperDto.Response mapperResponse
        ) {
            this.concertSeq = mapperResponse.concertSeq();
            this.ticketSeq = mapperResponse.ticketSeq();
            this.gradeName = mapperResponse.gradeName();
            this.totalTickets = mapperResponse.totalTickets();
            this.orderTry = mapperResponse.orderTry();
        }
    }
}
