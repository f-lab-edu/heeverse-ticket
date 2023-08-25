package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.service.TicketService;

import java.time.LocalDate;
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
    private final TicketService ticketService;

    @Transactional
    public void registerConcert(List<ConcertRequestDto> listDto) {
        for (ConcertRequestDto dto : listDto) {
            Long concertSeq = concertMapper.insertConcert(new Concert(dto));

            TicketRequestDto ticketRequestDto = new TicketRequestDto(concertSeq,
                LocalDate.now(),
                dto.getTicketGradeDtoList());
            ticketService.registerTicket(ticketRequestDto);
        }
    }
}
