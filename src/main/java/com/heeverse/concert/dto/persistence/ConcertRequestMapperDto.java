package com.heeverse.concert.dto.persistence;

import com.heeverse.common.Paging;
import com.heeverse.concert.dto.presentation.SearchConcertRequestDto;

/**
 * @author jeongheekim
 * @date 2023/08/23
 */
public class ConcertRequestMapperDto {

    private final String concertName;
    private final int page;
    private final int size;
    private final long offset;

    public ConcertRequestMapperDto(SearchConcertRequestDto dto) {
        Paging paging = dto.paging();
        this.concertName = dto.concertName();
        this.page = paging.getPageNumber();
        this.size = paging.getPageSize();
        this.offset = paging.getOffset();
    }
}
