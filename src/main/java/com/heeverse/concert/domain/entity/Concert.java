package com.heeverse.concert.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.concert.dto.ConcertRequestDto;
import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/04
 */
public class Concert extends BaseEntity {

    private Long concertId;
    private Long artistId;
    private Long venueId;
    private String concertName;
    private LocalDateTime concertDate;
    private LocalDateTime ticketOpenTime;
    private LocalDateTime ticketEndTime;
    private boolean deleteYn;


    public Concert(ConcertRequestDto dto) {
        this(dto.getArtistId(), dto.getVenueId(), dto.getConcertName(),
            dto.getConcertDate(), dto.getTicketOpenTime(), dto.getTicketEndTime());
    }

    public Concert(Long artistId, Long venueId, String concertName,
        LocalDateTime concertDate, LocalDateTime ticketOpenTime, LocalDateTime ticketEndTime) {
        this.artistId = artistId;
        this.venueId = venueId;
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketOpenTime = ticketOpenTime;
        this.ticketEndTime = ticketEndTime;
    }

}
