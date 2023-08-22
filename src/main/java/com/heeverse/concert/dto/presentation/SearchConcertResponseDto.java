package com.heeverse.concert.dto.presentation;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */
public record SearchConcertResponseDto(String concertName, LocalDateTime concertDate,
                                       String artistNameKor,
                                       String artistNameEng, String venueName) {

}
