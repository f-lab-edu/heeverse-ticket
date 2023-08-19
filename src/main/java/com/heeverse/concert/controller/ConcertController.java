package com.heeverse.concert.controller;

import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author jeongheekim
 * @date 2023/08/07
 */
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

}
