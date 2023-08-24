package com.heeverse.concert.dto.presentation;

import com.heeverse.concert.dto.persistence.ConcertResponseMapperDto;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */
@Getter
public class SearchConcertResponseDto {

    private final String concertName;
    private final LocalDateTime concertDate;
    private final String artistNameKor;
    private final String artistNameEng;
    private final String venueName;

    public SearchConcertResponseDto(ConcertResponseMapperDto dto) {
        this.concertName = dto.getConcertName();
        this.concertDate = dto.getConcertDate();
        this.artistNameKor = dto.getArtistNameKor();
        this.artistNameEng = dto.getArtistNameEng();
        this.venueName = dto.getVenueName();
    }

}
