package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.ticket.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@RequiredArgsConstructor
@Service
public class ConcertService {
    private final ConcertMapper concertMapper;
    private final TicketService ticketService;

    public void registerConcert(List<ConcertRequestDto> listDto) {
        //공연 생성 시
        for (ConcertRequestDto dto : listDto) {
            //콘서트 row 생성
            Concert concert = concertMapper.insertConcert(new Concert(dto));

            //tiket서비스 요청
            //ticketService.registerTicket(new TicketRequestDto(dto.get));
            //concert mapper insert
        }
    }
}
