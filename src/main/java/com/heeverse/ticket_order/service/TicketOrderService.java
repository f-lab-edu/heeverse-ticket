package com.heeverse.ticket_order.service;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.TicketNotNormallyUpdatedException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TicketOrderService {

    private final TicketService ticketService;
    private final TicketOrderMapper ticketOrderMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long orderTicket(TicketOrderRequestDto dto, Long memberSeq) throws Exception {

        List<Long> reqTicketSeqList = dto.ticketSetList();

        checkBookedTicket(dto, reqTicketSeqList);

        Long ticketOrderSeq = createTicketOrder(memberSeq);

        int updateCount = ticketService.updateTicketInfo(lockTicket(reqTicketSeqList), ticketOrderSeq);

        if (updateCount != reqTicketSeqList.size()) {
            throw new TicketNotNormallyUpdatedException("Ticket Table order_seq update fail!");
        }
        return ticketOrderSeq;
    }

    private List<Long> lockTicket(List<Long> reqTicketSeqList) throws Exception {
        List<Long> lockTicketSeqList = ticketService.ticketSelectForUpdate(reqTicketSeqList);
        lockTicketSeqList.forEach(seq -> log.info("[requested ticket seq] : {}", seq));
        return lockTicketSeqList;
    }

    private Long createTicketOrder(Long memberSeq) throws Exception {
        TicketOrder ticketOrder = new TicketOrder(memberSeq, LocalDateTime.now(), BookingStatus.DONE);
        ticketOrderMapper.insertTicketOrder(ticketOrder);
        return Optional.ofNullable(ticketOrder.getSeq()).orElseThrow(IllegalArgumentException::new);
    }

    private void checkBookedTicket(TicketOrderRequestDto dto, List<Long> reqTicketSeqList) {
        List<Ticket> availableTicketList = ticketService.getTicketsByTicketSeqList(reqTicketSeqList)
                .stream()
                .filter(d -> ObjectUtils.isEmpty(d.getOrderSeq()))
                .toList();
        int requestSize = dto.ticketSetList().size();
        if (requestSize != availableTicketList.size()) {
            log.error("요청 티켓 수 :{}, 예약 가능 티켓 수 : {}", requestSize, availableTicketList.size());
            throw new AlreadyBookedTicketException("이미 예약된 티켓이 포함되어 있습니다.");
        }
    }

    public List<TicketOrderResponseDto> getOrderTicket(Long ticketOrderSeq) {
        Assert.notNull(ticketOrderSeq);
        List<TicketOrderRequestMapperDto> dtoList = ticketOrderMapper.selectTicketOrderList(ticketOrderSeq);
        return dtoList.stream().map(TicketOrderResponseDto::new).collect(Collectors.toList());
    }
}
