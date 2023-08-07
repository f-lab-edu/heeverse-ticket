package com.heeverse.concert.domain.entity;

import com.heeverse.common.BaseEntity;
import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/04
 */
public class Concert extends BaseEntity {
    private Long concertId;
    private String artistName;
    private String concertName;
    private LocalDateTime concertDate;
    private LocalDateTime ticketOpenTime;
    private LocalDateTime ticketEndTime;
    private Long venueId;
    private boolean deleteYn;

}
