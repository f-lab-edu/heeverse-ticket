package com.heeverse.ticket.service;

import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.dto.persistence.TicketRequestMapperDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketRemainsResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketRemainsResponseMapperDto;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.LockOccupancyFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void registerTicket(TicketRequestDto ticketRequestDto) {

        if (existTicket(ticketRequestDto)) {
            throw new DuplicatedTicketException();
        }
        saveTicket(saveGradeTicket(ticketRequestDto), ticketRequestDto);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicket(long concertSeq) {
        return ticketMapper.findTickets(concertSeq);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByTicketSeqList(List<Long> ticketSeqList) {
        return ticketMapper.findTicketsByTicketSeqList(ticketSeqList);
    }

    private boolean existTicket(TicketRequestDto ticketRequestDto) {
        return 0 < ticketMapper.countTicket(ticketRequestDto.concertSeq());
    }


    private List<GradeTicket> saveGradeTicket(TicketRequestDto ticketRequestDto) {

        List<GradeTicket> gradeTickets = ticketRequestDto.ticketGradeDtoList().stream()
                .map(dto -> new GradeTicket(dto, ticketRequestDto.concertSeq()))
                .toList();

        ticketMapper.insertTicketGrade(gradeTickets);

        return gradeTickets;
    }

    private void saveTicket(List<GradeTicket> gradeTickets, TicketRequestDto ticketRequestDto) {

        List<Ticket> tickets = gradeTickets.stream()
                .flatMap(grade ->
                        IntStream.range(0, grade.getTicketCount())
                                .mapToObj(idx -> createTicket(ticketRequestDto, grade, idx)
                                )
                )
                .toList();

        ticketMapper.insertTicket(tickets);
    }

    private Ticket createTicket(TicketRequestDto ticketRequestDto, GradeTicket grade, int idx) {
        return new Ticket(new TicketSerialNumber(
                new TicketSerialTokenDto(ticketRequestDto.concertDate(),
                        ticketRequestDto.concertSeq(), grade, idx)), grade);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public int updateTicketInfo(List<Long> lockTicketSeqList, Long ticketOrderSeq) {
        validateTicketOrder(lockTicketSeqList, ticketOrderSeq);
        lockTicketSeqList.forEach(seq -> log.info("[requested ticket seq] : {}", seq));
        return ticketMapper.updateTicketOrderSeq(new TicketRequestMapperDto(lockTicketSeqList, ticketOrderSeq));
    }

    private void validateTicketOrder(List<Long> lockTicketSeqList, Long ticketOrderSeq) {
        if (ObjectUtils.isEmpty(lockTicketSeqList) || ObjectUtils.isEmpty(ticketOrderSeq)) {
            throw new IllegalArgumentException("When Ordering Ticket, ticketSeqList, ticketOrderSeq have to be NOT NULL");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.READ_COMMITTED)
    public void getTicketLock(List<Long> ticketSeqList) {
        log.info("[Ticket Lock] start record Lock");
        try {
            log.info("lock before tx name : {}", TransactionSynchronizationManager.getCurrentTransactionName());
            List<Ticket> lockedTicketList = ticketMapper.getTicketLock(ticketSeqList);
            log.info("lock after name : {}", TransactionSynchronizationManager.getCurrentTransactionName());
            log.info("ticketSeqList.size : {}, lockedTicketList.size : {}",ticketSeqList.size(), lockedTicketList.size());

        } catch (Exception e) {
            log.error("[Ticket Lock] fail get Lock");
            throw new LockOccupancyFailureException("해당 티켓 요청에 대한 LOCK 획득을 실패하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<TicketRemainsResponseDto> getTicketRemains(long concertSeq) {

        List<TicketRemainsResponseMapperDto> remainsTicketList = ticketMapper.aggregateTicketRemains(concertSeq, true);

        if (CollectionUtils.isEmpty(remainsTicketList)) {
            throw new IllegalArgumentException(concertSeq + " -> 발행된 티켓이 없어 집계 불가능 합니다.");
        }

        return remainsTicketList.stream()
                .map(TicketRemainsResponseDto::new)
                .collect(Collectors.toList());
    }

    public void rollbackTicketOrderSeq(List<Long> ticketSeqList) {
        int rollbackCount = ticketMapper.rollbackTicketOrderSeq(ticketSeqList);
        log.info("티켓 예매 rollback success count : {}", rollbackCount);
    }

    public void checkBookedTicket(TicketOrderRequestDto dto, List<Long> reqTicketSeqList) {
        List<Ticket> availableTicketList = ticketMapper.findTicketsByTicketSeqList(reqTicketSeqList)
                .stream()
                .filter(d -> ObjectUtils.isEmpty(d.getOrderSeq()))
                .toList();
        int requestSize = dto.ticketSetList().size();
        if (requestSize != availableTicketList.size()) {
            log.error("요청 티켓 수 :{}, 예약 가능 티켓 수 : {}", requestSize, availableTicketList.size());
            throw new AlreadyBookedTicketException("이미 예약된 티켓이 포함되어 있습니다.");
        }
    }
}
