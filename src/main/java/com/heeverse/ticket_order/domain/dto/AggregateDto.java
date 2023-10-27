package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
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
        private StrategyType strategyType;

        protected Request() {
        }
        public Request(Long concertSeq, boolean normalization, StrategyType strategyType) {
            this.concertSeq = concertSeq;
            this.normalization = normalization;
            this.strategyType = strategyType;
        }

        public boolean isQuery() {
            return strategyType == StrategyType.QUERY;
        }
    }


    @Getter
    public static class Response {

        private final Long concertSeq;
        private final String gradeName;
        private final Integer totalTickets;
        private final Integer orderTry;

        public Response(Long concertSeq, String gradeName, Integer totalTickets, Integer orderTry) {
            this.concertSeq = concertSeq;
            this.gradeName = gradeName;
            this.totalTickets = totalTickets;
            this.orderTry = orderTry;
        }

        public Response(
                AggregateSelectMapperDto.Response mapperResponse
        ) {
            this(mapperResponse.concertSeq(), mapperResponse.gradeName(), mapperResponse.totalTickets(), mapperResponse.orderTry());
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
