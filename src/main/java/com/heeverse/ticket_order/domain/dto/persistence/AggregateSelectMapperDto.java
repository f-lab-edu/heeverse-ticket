package com.heeverse.ticket_order.domain.dto.persistence;

/**
 * @author gutenlee
 * @since 2023/10/13
 */
public class AggregateSelectMapperDto {
    public record Request (
            Long concertSeq
    ) { }

    public record ZeroOffsetRequest (
            long concertSeq,
            long fromTicketSeq,
            long toTicketSeq,
            long offset,
            int size
    ) {

        public static ZeroOffsetRequest start(long concertSeq, long fromTicketSeq, long toTicketSeq) {
            return new ZeroOffsetRequest(concertSeq, fromTicketSeq, toTicketSeq, 0, 1);
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
    ) {}

    public record MinMaxResponse (
        long minSeq,
        long maxSeq
    ) { }
}
