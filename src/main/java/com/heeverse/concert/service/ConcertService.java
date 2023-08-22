package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.persistence.SearchConcertDto;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertResponseDto;
import com.heeverse.concert.exception.ConcertTimeValidationException;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Slf4j
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
                dto.getConcertDate(), dto.getTicketGradeDtoList());
            ticketService.registerTicket(ticketRequestDto);
        }
    }

    @Transactional(readOnly = true)
    public List<SearchConcertResponseDto> getConcertList(SearchConcertRequestDto dto) {

        List<SearchConcertDto> concertList = concertMapper.selectConcertList(dto);
        return concertList.stream().map(d -> new SearchConcertResponseDto(d.getConcertName(),
                d.getConcertDate(), d.getArtistNameKor(), d.getArtistNameEng(), d.getVenueName()))
            .collect(Collectors.toList());
    }
}
