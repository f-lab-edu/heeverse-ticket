package com.heeverse.concert.controller;

import com.heeverse.common.PagingRequest;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.dto.presentation.RegisteredConcertResponseDto;
import com.heeverse.concert.dto.presentation.SearchConcertRequestDto;
import com.heeverse.concert.dto.presentation.SearchConcertResponseDto;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.heeverse.common.IndexController.INDEX_URI;

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
    public ResponseEntity<List<RegisteredConcertResponseDto>> create(@RequestBody List<@Valid ConcertRequestDto> dtoList) {
        if (ObjectUtils.isEmpty(dtoList)) {
            throw new IllegalArgumentException("requestDto 값이 비어있습니다.");
        }
        List<Long> concertSeqList = concertService.registerConcert(dtoList);
        return ResponseEntity
                .created(URI.create(INDEX_URI))
                .body(concertService.getRegisteredConcertList(concertSeqList));
    }

    @GetMapping
    public ResponseEntity<List<SearchConcertResponseDto>> getConcertList(
        @RequestParam String concertName, @RequestParam int page, @RequestParam int size) {
        log.info("조회 concertName : {}, page: {}, size:{} ", concertName, page, size);
        return ResponseEntity.ok(
            concertService.getConcertList(
                new SearchConcertRequestDto(concertName, PagingRequest.of(page, size))));
    }

}
