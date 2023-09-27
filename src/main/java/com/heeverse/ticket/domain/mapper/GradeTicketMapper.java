package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.GradeTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jeongheekim
 * @date 2023/08/09
 */
@Mapper
public interface GradeTicketMapper {
    void insertGradeTicket(GradeTicket gradeTicket);
}
