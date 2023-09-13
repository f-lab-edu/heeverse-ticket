package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/02
 */
@Mapper
public interface TicketOrderMapper {
    void insertTicketOrder(TicketOrder ticketOrder);

    List<TicketOrderRequestMapperDto> selectTicketOrderList(Long ticketOrderSeq);
}
