package com.heeverse.common;

import com.heeverse.ticket.domain.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/09
 */
@Mapper
public interface LockMapper {
    List<Ticket> getLock(@Param("ticketSeqList") List<Long> ticketSeqList);

}
