package com.heeverse.ticket.domain.entity;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
public class OrderInfo {

    private final LocalDateTime purchaseDate;
    private final boolean cancelledFlag = false;
    private final Long orderSeq;

    public OrderInfo(LocalDateTime purchaseDate, Long orderSeq) {
        this.purchaseDate = purchaseDate;
        this.orderSeq = orderSeq;
    }


}
