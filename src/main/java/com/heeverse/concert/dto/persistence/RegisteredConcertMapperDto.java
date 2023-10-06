package com.heeverse.concert.dto.persistence;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 9/30/23
 */
public record RegisteredConcertMapperDto (String concertName, LocalDateTime concertDate,
                                         String artistNameKor, String artistNameEng,
                                         String venueName, int ticketCount){
}
