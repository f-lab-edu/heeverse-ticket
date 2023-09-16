package com.heeverse.common;

import com.heeverse.ticket.domain.entity.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/09
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LockTemplate {

    private final LockMapper lockMapper;

    public void getLock(List<Long> ticketSeqList) {
        log.info("[LockTemplate] start record Lock");
        List<Ticket> lockedTicketList = lockMapper.getLock(ticketSeqList);
        lockedTicketList.forEach(v -> log.info("lock target ticket seq : {}", v.getSeq()));
        log.info("[LockTemplate] success get Lock");
    }

}
