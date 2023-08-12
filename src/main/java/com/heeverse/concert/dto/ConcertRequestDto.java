package com.heeverse.concert.dto;

import com.heeverse.ticket.dto.TicketGradeDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Getter
public class ConcertRequestDto {

    @NotBlank(message = "공연명은 빈 문자열일 수 없습니다.")
    private String concertName;

    @Future(message = "현재 시간보다 이후여야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:Ss")
    private LocalDateTime concertDate;

    @Future(message = "현재 시간보다 이후여야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:Ss")
    private LocalDateTime ticketOpenTime;

    @Future(message = "현재 시간보다 이후여야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:Ss")
    private LocalDateTime ticketEndTime;

    @NotNull
    private Long artistId;

    @NotNull
    private Long venueId;

    private List<TicketGradeDto> ticketGradeDtoList;


}
