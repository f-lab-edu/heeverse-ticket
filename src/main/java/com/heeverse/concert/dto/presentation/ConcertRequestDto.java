package com.heeverse.concert.dto.presentation;

import com.heeverse.concert.domain.FutureDate;
import com.heeverse.ticket.dto.TicketGradeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertRequestDto {

    @NotBlank(message = "공연명은 빈 문자열일 수 없습니다.")
    private String concertName;

    @FutureDate
    private LocalDateTime concertDate;

    @FutureDate
    private LocalDateTime ticketOpenTime;

    @FutureDate
    private LocalDateTime ticketEndTime;

    @NotNull
    private Long artistSeq;

    @NotNull
    private Long venueSeq;

    private List<TicketGradeDto> ticketGradeDtoList;

    public ConcertRequestDto(String concertName, LocalDateTime concertDate,
        LocalDateTime ticketOpenTime, LocalDateTime ticketEndTime, @NotNull Long artistSeq,
        @NotNull Long venueSeq, List<TicketGradeDto> ticketGradeDtoList) {
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketOpenTime = ticketOpenTime;
        this.ticketEndTime = ticketEndTime;
        this.artistSeq = artistSeq;
        this.venueSeq = venueSeq;
        this.ticketGradeDtoList = ticketGradeDtoList;
    }
}
