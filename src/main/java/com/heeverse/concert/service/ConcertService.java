package com.heeverse.concert.service;

import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
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

    public void registerConcert(ConcertRequestDto dto) {
    }
}
