package com.heeverse.concert.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author jeongheekim
 * @date 2023/08/08
 */
class ConcertRequestDtoTest {

    //TODO 특정 유효성 검사를 통과한다로 세분화 하기
    @DisplayName("ConcertRequestDto 유효성 검사 통과한다.")
    @Test
    void requestDto_validation_success_test() {
        Long artistId = 3L;
        Long venueId = 1L;

        //ConcertRequestDto dto = new ConcertRequestDto();

    }
}