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
import com.heeverse.ticket_order.domain.exception.TicketNotNormallyUpdatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    public final int UPDATE_FAIL_COUNT = 0;
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

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = TicketNotNormallyUpdatedException.class)
    public void updateTicketInfo(List<Long> lockTicketSeqList, Long ticketOrderSeq) {
        validateTicketOrder(lockTicketSeqList, ticketOrderSeq);
        lockTicketSeqList.forEach(seq -> log.info("[requested ticket seq] : {}", seq));
        int updateCount = ticketMapper.updateTicketOrderSeq(new TicketRequestMapperDto(lockTicketSeqList, ticketOrderSeq));
        if (updateCount == UPDATE_FAIL_COUNT) {
            log.error("이미 예매 성공한 티켓으로 인해 티켓 테이블에 order_seq update 실패");
            throw new TicketNotNormallyUpdatedException("이미 예매 성공한 티켓으로 인해 티켓 테이블에 order_seq update 실패");
        }
    }

    private void validateTicketOrder(List<Long> lockTicketSeqList, Long ticketOrderSeq) {
        if (ObjectUtils.isEmpty(lockTicketSeqList) || ObjectUtils.isEmpty(ticketOrderSeq)) {
            throw new IllegalArgumentException("When Ordering Ticket, ticketSeqList, ticketOrderSeq have to be NOT NULL");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = LockOccupancyFailureException.class)
    public void getTicketLock(Long ticketOrderSeq, List<Long> ticketSeqList) {
        log.info("[Ticket Lock] start record Lock");
        try {
            List<Ticket> lockedTicketList = ticketMapper.getTicketLock(ticketSeqList);
            log.info("ticketSeqList.size : {}, lockedTicketList.size : {}", ticketSeqList.size(), lockedTicketList.size());

        } catch (Exception e) {
            log.error("[Ticket Lock] fail get Lock : {}", e.getMessage());
            throw new LockOccupancyFailureException("해당 티켓 요청에 대한 LOCK 획득을 실패하였습니다.");
        }
        log.info("[Ticket Lock] success get Lock : {}, ticketSeqList : {} ", ticketOrderSeq, ticketSeqList);
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

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = AlreadyBookedTicketException.class)
    public void checkBookedTicket(TicketOrderRequestDto dto, List<Long> reqTicketSeqList) {
        List<Ticket> availableTicketList = ticketMapper.findTicketsByTicketSeqList(reqTicketSeqList)
                .stream()
                .filter(Ticket::isNotOrdered)
                .toList();
        int requestSize = dto.ticketSetList().size();
        if (requestSize != availableTicketList.size()) {
            log.error("요청 티켓 수 :{}, 예약 가능 티켓 수 : {}", requestSize, availableTicketList.size());
            throw new AlreadyBookedTicketException("이미 예약된 티켓이 포함되어 있습니다.");
        }
    }
}
