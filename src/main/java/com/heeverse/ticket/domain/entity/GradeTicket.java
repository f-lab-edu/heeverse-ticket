package com.heeverse.ticket.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.ticket.dto.GradeTicketDto;

/**
 * @author jeongheekim
 * @date 2023/08/09
 */
public class GradeTicket extends BaseEntity {
    private Long seq;
    private String gradeName;
    private Integer ticketCount;
    private Long concertId;

    public GradeTicket(GradeTicketDto dto, Long concertId) {
        this.gradeName = dto.getGradeName();
        this.ticketCount = dto.getTicketCount();
        this.concertId = concertId;
    }
}
