package com.heeverse.concert.domain.entity;

import com.heeverse.common.BaseEntity;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.exception.ConcertTimeValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/04
 */
@Slf4j
@Getter
public class Concert extends BaseEntity {

    private Long seq;
    private Long artistSeq;
    private Long venueSeq;
    private String concertName;
    private LocalDateTime concertDate;
    private LocalDateTime ticketOpenTime;
    private LocalDateTime ticketEndTime;
    private boolean deleteYn;


    public Concert(ConcertRequestDto dto) {

        LocalDateTime ticketOpenTime = dto.getTicketOpenTime();
        LocalDateTime ticketEndTime = dto.getTicketEndTime();
        LocalDateTime concertDate = dto.getConcertDate();

        validateTimeOrder(ticketOpenTime, ticketEndTime,
            "티켓 종료 시간은 티켓 오픈 시간보다 이후여야 합니다.");
        validateTimeOrder(ticketEndTime, concertDate,
            "콘서트 날짜와시간은 티켓 종료 시간보다 이후여야 합니다.");

        this.artistSeq = dto.getArtistSeq();
        this.venueSeq = dto.getVenueSeq();
        this.concertName = dto.getConcertName();
        this.concertDate = dto.getConcertDate();
        this.ticketOpenTime = ticketOpenTime;
        this.ticketEndTime = ticketEndTime;
    }

    public Concert(Long artistSeq, Long venueSeq, String concertName, LocalDateTime concertDate, LocalDateTime ticketOpenTime, LocalDateTime ticketEndTime) {
        this.artistSeq = artistSeq;
        this.venueSeq = venueSeq;
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketOpenTime = ticketOpenTime;
        this.ticketEndTime = ticketEndTime;
    }

    private void validateTimeOrder(LocalDateTime beforeTime, LocalDateTime afterTime, String msg) {
        if (afterTime.isBefore(beforeTime)) {
            log.error("afterTime : {}이 befoerTime : {} 보다 미래여야하는 조건 위배", afterTime, beforeTime);
            throw new ConcertTimeValidationException(msg);
        }
    }

}
