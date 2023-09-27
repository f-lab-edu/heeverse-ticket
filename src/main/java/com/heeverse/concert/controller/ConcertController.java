package com.heeverse.concert.controller;

import com.heeverse.common.PagingRequest;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertResponseDto;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/concert")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody List<@Valid ConcertRequestDto> dtoList) {
        if (ObjectUtils.isEmpty(dtoList)) {
            throw new IllegalArgumentException("requestDto 값이 비어있습니다.");
        }

        concertService.registerConcert(dtoList);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SearchConcertResponseDto>> getConcertList(
        @RequestParam String concertName, @RequestParam int page, @RequestParam int size) {
        log.info("조회 concertName : {}, page: {}, size:{} ", concertName, page, size);
        return ResponseEntity.ok(
            concertService.getConcertList(
                new SearchConcertRequestDto(concertName, PagingRequest.of(page, size))));
    }

}
