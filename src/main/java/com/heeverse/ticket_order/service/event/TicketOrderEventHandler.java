package com.heeverse.ticket_order.service.event;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket_order.domain.entity.TicketOrderLog;
import com.heeverse.ticket_order.domain.mapper.TicketOrderLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gutenlee
 * @since 2023/10/14
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TicketOrderEventHandler {

    private final TicketOrderLogMapper logMapper;
    private final TicketMapper ticketMapper;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveTicketOrderLog(TicketOrderEvent event) {

        log.info("======= [Ticket Order Log] =======");
        List<Long> ticketSetList = event.dto().ticketSetList();
        log.info("======= {} =======", ticketSetList);

        List<TicketOrderLog> ticketOrderLogs = getTickets(ticketSetList).stream()
                .map(ticket -> new TicketOrderLog(ticket, event.memberSeq(), event.ticketOrderSeq()))
                .collect(Collectors.toList());

        logMapper.insertTicketOrderLogDeNormalization(ticketOrderLogs);
    }


    @Transactional(readOnly = true)
    public List<Ticket> getTickets(List<Long> ticketSeqList) {
        return ticketMapper.findTicketsByTicketSeqList(ticketSeqList);
    }
}
