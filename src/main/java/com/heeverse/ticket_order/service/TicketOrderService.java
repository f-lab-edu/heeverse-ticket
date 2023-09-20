package com.heeverse.ticket_order.service;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.service.TicketService;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderUpdateMapperDto;
import com.heeverse.ticket_order.domain.entity.TicketOrder;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.TicketNotNormallyUpdatedException;
import com.heeverse.ticket_order.domain.mapper.TicketOrderMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private BookingStatus bookingStatus = BookingStatus.SUCCESS;

    @Transactional
    public void orderTicket(TicketOrderRequestDto dto, Long ticketOrderSeq) throws Exception {
        List<Long> reqTicketSeqList = dto.ticketSetList();
        try {
            checkBookedTicket(dto, reqTicketSeqList);
            int updateCount = ticketService.updateTicketInfo(reqTicketSeqList, ticketOrderSeq);
            if (updateCount != reqTicketSeqList.size()) {
                log.error("Fail Update TicketOrderSeq");
                throw new TicketNotNormallyUpdatedException("티켓 테이블에 order_seq update 실패");
            }
        } catch (Exception e) {
            bookingStatus = BookingStatus.FAIL;
        } finally {
            changeTicketOrderStatus(new TicketOrderUpdateMapperDto(ticketOrderSeq, bookingStatus));
        }
    }

    public void changeTicketOrderStatus(TicketOrderUpdateMapperDto dto) {
        ticketOrderMapper.updateTicketOrderStatus(dto);
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
