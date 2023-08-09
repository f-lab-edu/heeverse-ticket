package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.ticket.TicketService;
import com.heeverse.ticket.domain.mapper.GradeTicketMapper;
import com.heeverse.ticket.dto.GradeTicketDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
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
        //공연 생성 시
        for (ConcertRequestDto dto : listDto) {
            //콘서트 row 생성
            Concert concert = concertMapper.insertConcert(new Concert(dto));
            Long concertId = concert.getConcertId();
            List<GradeTicketDto> gradeTicketDtoList = dto.getGradeTicketDtoList();
            for (GradeTicketDto gradeDto: gradeTicketDtoList) {
                gradeTicketMapper.insertGradeTicket(new GradeTicket(gradeDto, concertId));
            }

        }
            //tiket서비스 요청
            //ticketService.registerTicket(new TicketRequestDto(dto.get));
        }
}
