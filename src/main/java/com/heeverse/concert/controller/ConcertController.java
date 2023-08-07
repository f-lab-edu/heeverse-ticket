package com.heeverse.concert.controller;

import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.concert.dto.ConcertResponseDto;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@RequiredArgsConstructor
@RestController("/concert")
public class ConcertController {
    private final ConcertService concertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ConcertRequestDto dto) {
        concertService.registerConcert(dto);
    }

}
