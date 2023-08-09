package com.heeverse.concert.dto;

import com.heeverse.ticket.dto.GradeTicketDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Getter
public class ConcertRequestDto {

    @NotBlank(message = "공연명은 빈 문자열일 수 없습니다.")
    private String concertName;

    @Future(message = "현재 시간보다 이후여야합니다.")
    private LocalDateTime concertDate;

    @Future(message = "현재 시간보다 이후여야합니다.")
    private LocalDateTime ticketOpenTime;

    @Future(message = "현재 시간보다 이후여야합니다.")
    private LocalDateTime ticketEndTime;

    @NotNull
    private Long artistId;

    @NotNull
    private Long venueId;

    private List<GradeTicketDto> gradeTicketDtoList;


}
