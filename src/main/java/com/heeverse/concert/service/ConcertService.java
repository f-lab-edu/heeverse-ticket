package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.ticket.domain.mapper.GradeTicketMapper;
import com.heeverse.ticket.dto.GradeTicketDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@RequiredArgsConstructor
@Service
public class ConcertService {

    private final ConcertMapper concertMapper;
    private final GradeTicketMapper gradeTicketMapper;
    private final TicketService ticketService;

    @Transactional
    public void registerConcert(List<ConcertRequestDto> listDto) {
        for (ConcertRequestDto dto : listDto) {
            Concert concert = concertMapper.insertConcert(new Concert(dto));
            Long concertId = concert.getConcertId();
            List<GradeTicketDto> gradeTicketDtoList = dto.getGradeTicketDtoList();
            for (GradeTicketDto gradeDto : gradeTicketDtoList) {
                gradeTicketMapper.insertGradeTicket(new GradeTicket(gradeDto, concertId));
            }
        }
        //ticket 서비스 요청
    }
}
