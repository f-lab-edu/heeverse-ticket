package com.heeverse.ticket_order.service;

import com.heeverse.common.LockTemplate;
import com.heeverse.common.util.StringUtils;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

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
    private final Duration lockTimeout = Duration.ofSeconds(30);

    @Transactional
    public List<TicketOrderResponseDto> startTicketOrderJob(TicketOrderRequestDto dto, Long memberSeq) {
        String lockName = StringUtils.createSeqListStr(dto.ticketSetList(),"_");
        lockTemplate.getLock(lockName, (int) lockTimeout.getSeconds());
        try {
            Long ticketOrderSeq = ticketOrderService.orderTicket(dto, memberSeq);
            return ticketOrderService.getOrderTicket(ticketOrderSeq);
        } catch (Exception e) {
            log.error("티켓 예매 실패 : {}", e.getCause());
            throw new TicketingFailException(e.getMessage(), e);
        } finally {
            lockTemplate.releaseLock(lockName);
        }
    }
}
