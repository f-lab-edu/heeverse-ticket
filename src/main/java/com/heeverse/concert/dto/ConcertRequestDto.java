package com.heeverse.concert.dto;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
public class ConcertRequestDto {
    private String artistName;
    private String concertName;
    private LocalDateTime concertDate;
    private LocalDateTime ticketOpenTime;
    private LocalDateTime ticketEndTime;

    private Long venueId;
}
