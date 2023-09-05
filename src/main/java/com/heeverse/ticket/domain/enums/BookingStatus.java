package com.heeverse.ticket.domain.enums;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
public enum BookingStatus {
    DONE("예매 완료"), CANCEL("예매 취소");
    private String status;

    BookingStatus(String s) {
        this.status = s;
    }

    public String getStatus() {
        return status;
    }
}
