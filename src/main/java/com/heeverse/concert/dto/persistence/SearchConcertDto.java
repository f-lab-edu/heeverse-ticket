package com.heeverse.concert.dto.persistence;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */
@Getter
public class SearchConcertDto {
    private String concertName;
    private LocalDateTime concertDate;
    private String artistNameKor;
    private String artistNameEng;
    private String venueName;
}
