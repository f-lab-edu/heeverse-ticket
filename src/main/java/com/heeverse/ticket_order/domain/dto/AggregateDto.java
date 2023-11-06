package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

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
        private int size;

        protected Request() {
        }

        public Request(Long concertSeq, boolean normalization, StrategyType strategyType) {
            this(concertSeq, normalization, strategyType, 50);
        }

        public Request(Long concertSeq, boolean normalization, StrategyType strategyType, int size) {
            this.concertSeq = concertSeq;
            this.normalization = normalization;
            this.strategyType = strategyType;
            this.size = size;
        }

        public boolean isQuery() {
            return strategyType == StrategyType.QUERY;
        }
    }


    @Getter
    public static class Response {
        private Long concertSeq;
        private String gradeName;
        private Integer totalTickets;
        private Integer orderTry;
        private String message;
        private final LocalDateTime createdAt = LocalDateTime.now();

        public Response(
                @Nullable Long concertSeq,
                @Nullable String gradeName,
                @Nullable Integer totalTickets,
                @Nullable Integer orderTry
        ) {
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

        public Response(String message) {
            this.message = message;
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
