package com.heeverse.ticket.domain.enums;

import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
@Getter
public enum BookingStatus {
    DONE("예매 완료"),
    CANCEL("예매 취소");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

}
