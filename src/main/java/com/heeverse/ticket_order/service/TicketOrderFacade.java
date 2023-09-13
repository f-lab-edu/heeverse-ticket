package com.heeverse.ticket_order.service;

import com.heeverse.common.LockTemplate;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/09/13
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TicketOrderFacade {
    private final LockTemplate lockTemplate;
    private final TicketOrderService ticketOrderService;
    private static final String lockName = "heeverse";
    private static final int timeoutSeconds = 30;

    @Transactional
    public Long startTicketOrderJob(TicketOrderRequestDto dto, Long memberSeq) {
        lockTemplate.getLock(lockName, timeoutSeconds);
        try {
            return ticketOrderService.orderTicket(dto, memberSeq);
        } catch (Exception e) {
            log.error("티켓 예매 실패 : {}", e.getCause());
            throw new TicketingFailException(e.getMessage(), e);
        } finally {
            lockTemplate.releaseLock(lockName);
        }
    }
}
