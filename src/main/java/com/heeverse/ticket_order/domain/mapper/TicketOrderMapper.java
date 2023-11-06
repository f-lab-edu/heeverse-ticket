package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.config.LockScanMapper;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderUpdateMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/02
 */
@Mapper
@LockScanMapper
public interface TicketOrderMapper {
    void insertTicketOrder(TicketOrder ticketOrder);

    List<TicketOrderRequestMapperDto> selectTicketOrderList(Long ticketOrderSeq);

    List<TicketOrder> selectTicketOrderListByMemberSeq(Long memberSeq);

    void updateTicketOrderStatus(TicketOrderUpdateMapperDto dto);
}
