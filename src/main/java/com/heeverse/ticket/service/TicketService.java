package com.heeverse.ticket.service;

import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.dto.persistence.TicketRequestMapperDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket_order.domain.exception.LockOccupancyFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
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

    public void getTicketLock(List<Long> ticketSeqList) {
        log.info("[Ticket Lock] start record Lock");
        try {
            List<Ticket> lockedTicketList = ticketMapper.getTicketLock(ticketSeqList);
            lockedTicketList.forEach(v -> log.info("lock target ticket seq : {}", v.getSeq()));
            log.info("[Ticket Lock] success get Lock");

        } catch (Exception e) {
            log.error("[Ticket Lock] fail get Lock");
            throw new LockOccupancyFailureException("해당 티켓 요청에 대한 LOCK 획득을 실패하였습니다.");
        }
    }

    public void rollbackTicketOrderSeq(List<Long> ticketSeqList) {
        int rollbackCount = ticketMapper.rollbackTicketOrderSeq(ticketSeqList);
        log.info("티켓 예매 rollback success count : {}", rollbackCount);
    }
}
