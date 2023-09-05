package com.heeverse.ticket_order.service;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.TicketNormallyUpdatedException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(timeout = 20)
    public Long orderTicket(TicketOrderRequestDto dto, Member member) {

        List<Long> reqTicketSeqList = dto.ticketSetList();
        checkBookedTicket(dto, ticketService.getTicketsByTicketSeqList(reqTicketSeqList));

        Long ticketOrderSeq = createTicketOrder(member);

        int updateCount = ticketService.updateTicketOrderSeq(lockTicket(reqTicketSeqList), ticketOrderSeq);
        if(updateCount != reqTicketSeqList.size()) {
            throw new TicketNormallyUpdatedException("티켓 업데이트가 정상적으로 이루어지지 않았습니다.");
        }
        return ticketOrderSeq;
    }

    private List<Long> lockTicket(List<Long> reqTicketSeqList) {
        List<Long> lockTicketSeqList = ticketService.ticketSelectForUpdate(reqTicketSeqList);
        lockTicketSeqList.forEach(seq -> log.info("[requested ticket seq] : {}", seq));
        return lockTicketSeqList;
    }

    private Long createTicketOrder(Member member) {
        TicketOrder ticketOrder = new TicketOrder(member.getSeq(), LocalDateTime.now(), BookingStatus.DONE);
        ticketOrderMapper.insertTicketOrder(ticketOrder);
        return Optional.ofNullable(ticketOrder.getSeq()).orElseThrow(IllegalArgumentException::new);
    }

    private void checkBookedTicket(TicketOrderRequestDto dto, List<Ticket> ticketsByTicketSeqList) {
        int requestSize = dto.ticketSetList().size();
        if (requestSize > ticketsByTicketSeqList.size()) {
            throw new AlreadyBookedTicketException("이미 예약된 티켓이 포함되어 있습니다.");
        }
    }

    public List<TicketOrderResponseDto> getOrderTicket(Long ticketOrderSeq) {
        Assert.notNull(ticketOrderSeq);
        List<TicketOrderRequestMapperDto> dtoList = ticketOrderMapper.selectTicketOrderList(ticketOrderSeq);
        return dtoList.stream().map(TicketOrderResponseDto::new).collect(Collectors.toList());
    }
}
