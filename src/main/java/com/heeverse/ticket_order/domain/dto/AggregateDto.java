package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/10/12
 */
public class AggregateDto {


    @Getter
    public static class Request {

        private static final int DEFAULT_SIZE = 50;
        private Long concertSeq;
        private boolean normalization;
        private StrategyType strategyType;
        private int pageSize;

        protected Request(){};

        public Request(Long concertSeq, boolean normalization, StrategyType strategyType) {
            this(concertSeq, normalization, strategyType, DEFAULT_SIZE);
        }

        public Request(Long concertSeq, boolean normalization, StrategyType strategyType, int pageSize) {
            this.concertSeq = concertSeq;
            this.normalization = normalization;
            this.strategyType = strategyType;
            this.pageSize = pageSize <= 0 ? DEFAULT_SIZE : pageSize;
        }

        public boolean isQuery() {
            return strategyType == StrategyType.QUERY;
        }
    }


    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long concertSeq;
        private String gradeName;
        private Integer totalTickets;
        private Integer orderTry;
        private String message = "집계 작업이 시작되었습니다";
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
