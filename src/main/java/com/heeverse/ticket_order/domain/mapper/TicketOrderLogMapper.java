package com.heeverse.ticket_order.domain.mapper;

import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/02
 */
@Mapper
public interface TicketOrderLogMapper {
    void insertTicketOrderLog(List<TicketOrderLog> ticketOrderLogList);
    void insertTicketOrderLogDeNormalization(List<TicketOrderLog> ticketOrderLogList);

}
