package com.heeverse.ticket_order.domain.dto;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;

/**
 * @author gutenlee
 * @since 2023/11/07
 */
public record StrategyDto (
        StrategyType strategyType,
        int pageSize
) {
}
