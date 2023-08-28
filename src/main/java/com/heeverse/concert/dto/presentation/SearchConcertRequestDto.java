package com.heeverse.concert.dto.presentation;

import com.heeverse.common.Paging;

/**
 * @author jeongheekim
 * @date 2023/08/22
 */
public record SearchConcertRequestDto(String concertName, Paging paging) {

}
