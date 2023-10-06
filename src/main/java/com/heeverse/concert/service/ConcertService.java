package com.heeverse.concert.service;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.persistence.ConcertRequestMapperDto;
import com.heeverse.concert.dto.persistence.ConcertResponseMapperDto;
import com.heeverse.concert.dto.persistence.RegisteredConcertMapperDto;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.dto.presentation.RegisteredConcertResponseDto;
import com.heeverse.concert.dto.presentation.SearchConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertResponseDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Long> registerConcert(List<ConcertRequestDto> listDto) {
        List<Long> concertList = new ArrayList<>();
        for (ConcertRequestDto dto : listDto) {
            Concert concert = new Concert(dto);
            concertMapper.insertConcert(concert);
            long concertSeq = Optional.of(concert.getSeq()).orElseThrow(IllegalArgumentException::new);
            createTicket(dto, concertSeq);
            concertList.add(concertSeq);
        }
        return concertList;
    }

    private void createTicket(ConcertRequestDto dto, long concertSeq) {
        TicketRequestDto ticketRequestDto = new TicketRequestDto(concertSeq, dto.getConcertDate(), dto.getTicketGradeDtoList());
        ticketService.registerTicket(ticketRequestDto);
    }

    @Transactional(readOnly = true)
    public List<SearchConcertResponseDto> getConcertList(SearchConcertRequestDto dto) {

        List<ConcertResponseMapperDto> concertList = concertMapper.selectConcertList(new ConcertRequestMapperDto(dto));
        return concertList.stream().map(SearchConcertResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegisteredConcertResponseDto> getRegisteredConcertList(List<Long> concertSeqList) {
        List<RegisteredConcertMapperDto> dtoList = concertMapper.selectRegisteredConcertList(concertSeqList);
        return dtoList.stream().map(RegisteredConcertResponseDto::new)
                .collect(Collectors.toList());
    }
}
