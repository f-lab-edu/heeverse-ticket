package com.heeverse.ticket_order.domain.dto.persistence;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
public class AggregateSelectMapperDto {
    public record Request (
            Long concertSeq
    ) { }

    public record Response (
            Long concertSeq,
            String gradeName,
            Integer totalTickets,
            Integer orderTry
    ) { }


    public record SimpleResponse (
            long seq,
            long ticketSeq
    ) {
        public SimpleResponse(long seq, long ticketSeq) {
            this.seq = seq;
            this.ticketSeq = ticketSeq;
        }
    }

}
