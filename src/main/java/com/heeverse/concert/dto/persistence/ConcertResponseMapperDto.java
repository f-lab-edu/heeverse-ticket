package com.heeverse.concert.dto.persistence;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */

public record ConcertResponseMapperDto(String concertName, LocalDateTime concertDate,
                                       String artistNameKor, String artistNameEng,
                                       String venueName) {

}
