package com.heeverse.ticket.domain.entity;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
public class OrderInfo {

    private final LocalDateTime purchaseDateTime;
    private final boolean isCancelled = false;
    private final Long orderSeq;

    public OrderInfo(LocalDateTime purchaseDateTime, Long orderSeq) {
        this.purchaseDateTime = purchaseDateTime;
        this.orderSeq = orderSeq;
    }


}
