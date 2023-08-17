package com.heeverse.concert.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.concert.dto.ConcertRequestDto;
import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/04
 */
public class Concert extends BaseEntity {

    private Long concertSeq;
    private Long artistSeq;
    private Long venueSeq;
    private String concertName;
    private LocalDateTime concertDate;
    private LocalDateTime ticketOpenTime;
    private LocalDateTime ticketEndTime;
    private boolean deleteYn;


    public Concert(ConcertRequestDto dto) {
        this(dto.getArtistSeq(), dto.getVenueSeq(), dto.getConcertName(),
            dto.getConcertDate(), dto.getTicketOpenTime(), dto.getTicketEndTime());
    }

    public Concert(Long artistSeq, Long venueSeq, String concertName,
        LocalDateTime concertDate, LocalDateTime ticketOpenTime, LocalDateTime ticketEndTime) {
        this.artistSeq = artistSeq;
        this.venueSeq = venueSeq;
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketOpenTime = ticketOpenTime;
        this.ticketEndTime = ticketEndTime;
    }

    public Long getConcertSeq() {
        return concertSeq;
    }
}
