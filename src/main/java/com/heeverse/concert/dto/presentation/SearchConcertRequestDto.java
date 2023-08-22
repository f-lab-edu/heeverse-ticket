package com.heeverse.concert.dto.presentation;

import jakarta.validation.constraints.Min;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */
public class SearchConcertRequestDto {
    private String concertName;
    @Min(1)
    private int page;
    @Min(1)
    private int cnt;

    public SearchConcertRequestDto(String concertName, Integer page, Integer cnt) {
        this.concertName = concertName;
        this.page = page;
        this.cnt = cnt;
    }
}
