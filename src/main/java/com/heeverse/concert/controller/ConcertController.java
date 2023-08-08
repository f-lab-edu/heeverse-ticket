package com.heeverse.concert.controller;

import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.concert.exception.ConcertTimeValidationException;
import com.heeverse.concert.service.ConcertService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public void create(@RequestBody List<@Valid ConcertRequestDto> dtoList) {
        if (dtoList.size() == 0) {
            throw new IllegalArgumentException("requestDto 값이 비어있습니다.");
        }

        validationConcertDto(dtoList);
        concertService.registerConcert(dtoList);
    }

    private void validationConcertDto(List<ConcertRequestDto> dtoList) {
        for (ConcertRequestDto dto : dtoList) {
            if (dto.getTicketEndTime().isBefore(dto.getTicketOpenTime())) {
                throw new ConcertTimeValidationException("티켓 종료 시간은 티켓 오픈 시간보다 이후여야 합니다.");
            }

            if (dto.getConcertDate().isBefore(dto.getTicketEndTime())) {
                throw new ConcertTimeValidationException("콘서트 날짜와시간은 티켓 종료 시간보다 이후여야 합니다.");
            }
        }
    }

}
