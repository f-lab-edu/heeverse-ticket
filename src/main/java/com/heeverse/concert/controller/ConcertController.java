package com.heeverse.concert.controller;

import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.concert.exception.ConcertTimeValidationException;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/concert")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody List<@Valid ConcertRequestDto> dtoList) {
        if (ObjectUtils.isEmpty(dtoList)) {
            throw new IllegalArgumentException("requestDto 값이 비어있습니다.");
        }

        validationConcertDto(dtoList);
        concertService.registerConcert(dtoList);
    }

    private void validationConcertDto(List<ConcertRequestDto> dtoList) {
        for (ConcertRequestDto dto : dtoList) {
            validateTimeOrder(dto.getTicketEndTime(), dto.getTicketOpenTime(),"티켓 종료 시간은 티켓 오픈 시간보다 이후여야 합니다.");
            validateTimeOrder(dto.getConcertDate(), dto.getTicketEndTime(),"콘서트 날짜와시간은 티켓 종료 시간보다 이후여야 합니다.");
        }
    }

    private static void validateTimeOrder(LocalDateTime beforeTime, LocalDateTime afterTime, String msg) {
        if (beforeTime.isBefore(afterTime)) {
            throw new ConcertTimeValidationException(msg);
        }

    }

}
