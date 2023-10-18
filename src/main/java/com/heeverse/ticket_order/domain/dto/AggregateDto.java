package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import lombok.Getter;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
public class AggregateDto {


    @Getter
    public static class Request {

        private Long concertSeq;
        private boolean normalization;
        private boolean multithreading;

        protected Request() {
        }
        public Request(Long concertSeq, boolean normalization, boolean multithreading) {
            this.concertSeq = concertSeq;
            this.normalization = normalization;
            this.multithreading = multithreading;
        }
    }


    @Getter
    public static class Response {

        private final Long concertSeq;
        private final String gradeName;
        private final Integer totalTickets;
        private final Integer orderTry;

        public Response(
                AggregateSelectMapperDto.Response mapperResponse
        ) {
            this.concertSeq = mapperResponse.concertSeq();
            this.gradeName = mapperResponse.gradeName();
            this.totalTickets = mapperResponse.totalTickets();
            this.orderTry = mapperResponse.orderTry();
        }

        @Override
        public String toString() {
            return "Response{" +
                    "concertSeq=" + concertSeq +
                    ", gradeName='" + gradeName + '\'' +
                    ", totalTickets=" + totalTickets +
                    ", orderTry=" + orderTry +
                    '}';
        }
    }
}
