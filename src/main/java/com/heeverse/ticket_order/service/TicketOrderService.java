package com.heeverse.ticket_order.service;

import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderUpdateMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.exception.TicketOrderException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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


    @Transactional(noRollbackFor = TicketOrderException.class, isolation = Isolation.READ_COMMITTED)
    public void orderTicket(TicketOrderRequestDto dto, Long ticketOrderSeq) {

        List<Long> reqTicketSeqList = dto.ticketSetList();
        BookingStatus bookingStatus = BookingStatus.SUCCESS;

        try {
            ticketService.checkBookedTicket(dto, dto.ticketSetList());
            ticketService.getTicketLock(ticketOrderSeq, dto.ticketSetList());
            ticketService.checkBookedTicket(dto, reqTicketSeqList);
            ticketService.updateTicketInfo(reqTicketSeqList, ticketOrderSeq);

        } catch (Exception e) {
            log.error("TicketOrderService - orderTicket 실패 : {} , ticketSeqList :{} ", e.toString(), reqTicketSeqList);
            bookingStatus = BookingStatus.FAIL;
            throw new TicketOrderException(e);

        } finally {
            changeTicketOrderStatus(new TicketOrderUpdateMapperDto(ticketOrderSeq, bookingStatus));
        }
    }


     private void changeTicketOrderStatus(TicketOrderUpdateMapperDto dto) {
        ticketOrderMapper.updateTicketOrderStatus(dto);
    }

    @Transactional(readOnly = true)
    public List<TicketOrderResponseDto> getOrderTicket(Long ticketOrderSeq) {
        Assert.notNull(ticketOrderSeq);
        List<TicketOrderRequestMapperDto> dtoList = ticketOrderMapper.selectTicketOrderList(ticketOrderSeq);
        return dtoList.stream().map(TicketOrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long createTicketOrder(Long memberSeq) {
        TicketOrder ticketOrder = new TicketOrder(memberSeq, LocalDateTime.now(), BookingStatus.READY);
        ticketOrderMapper.insertTicketOrder(ticketOrder);
        Long ticketOrderSeq = ticketOrder.getSeq();
        log.info("createTicketOrder - ticket order seq : {}", ticketOrderSeq);
        return Optional.ofNullable(ticketOrderSeq).orElseThrow(IllegalArgumentException::new);
    }


}
