package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/13
 */
public class TicketOrderTestHelper {

    public static TicketOrderRequestDto createTicketOrderRequestDto(List<Long> ticketSeqList) {
        return new TicketOrderRequestDto(ticketSeqList);
    }


    public static List<List<Long>> partitionSeq(List<Long> list, int partitionSize) {

        List<List<Long>> partitions = new ArrayList<>();

        for (int i = 0; i < list.size(); i += partitionSize) {
            partitions.add(list.subList(i, Math.min(i + partitionSize, list.size())));
        }

        return partitions;
    }

}
