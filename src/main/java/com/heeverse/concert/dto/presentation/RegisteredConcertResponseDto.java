package com.heeverse.concert.dto.presentation;

import com.heeverse.concert.dto.persistence.RegisteredConcertMapperDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 9/30/23
 */
@Getter
public class RegisteredConcertResponseDto {
    private final String concertName;
    private final LocalDateTime concertDate;
    private final String artistNameKor;
    private final String artistNameEng;
    private final String venueName;
    private final int ticketCount;

    public RegisteredConcertResponseDto(RegisteredConcertMapperDto dto) {
        this.concertName = dto.concertName();
        this.concertDate = dto.concertDate();
        this.artistNameKor = dto.artistNameKor();
        this.artistNameEng = dto.artistNameEng();
        this.venueName = dto.venueName();
        this.ticketCount = dto.ticketCount();
    }


}
