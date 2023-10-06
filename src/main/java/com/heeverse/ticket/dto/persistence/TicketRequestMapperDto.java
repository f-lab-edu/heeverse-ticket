package com.heeverse.ticket.dto.persistence;

import lombok.Getter;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/04
 */
public record TicketRequestMapperDto(List<Long> lockTicketSeqList, Long ticketOrderSeq) {
}
