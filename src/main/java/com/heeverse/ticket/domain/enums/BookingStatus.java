package com.heeverse.ticket.domain.enums;

import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
@Getter
public enum BookingStatus {

    READY("예매 대기"),
    SUCCESS("예매 성공"),
    FAIL("예매 실패"),
    CANCEL("예매 취소");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

}
