package com.heeverse.ticket.domain.entity;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
public class OrderInfo {

    private final LocalDateTime purchaseDateTime;
    private final boolean cancelled = false;
    private final Long orderId;

    public OrderInfo(LocalDateTime purchaseDateTime, Long orderId) {
        this.purchaseDateTime = purchaseDateTime;
        this.orderId = orderId;
    }


}
