package com.heeverse.concert.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.concert.dto.ConcertRequestDto;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/04
 */
@Getter
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
        super();
        this.artistId = dto.getArtistId();
        this.venueId = dto.getVenueId();
        this.concertName = dto.getConcertName();
        this.concertDate = dto.getConcertDate();
        this.ticketOpenTime = dto.getTicketOpenTime();
        this.ticketEndTime = dto.getTicketEndTime();
    }

    public Long getConcertId() {
        return concertId;
    }
}
