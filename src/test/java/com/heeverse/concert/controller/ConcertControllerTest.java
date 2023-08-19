package com.heeverse.concert.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.ticket.dto.TicketGradeDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author jeongheekim
 * @date 2023/08/10
 */
@WebMvcTest(ConcertController.class)
class ConcertControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConcertService concertService;
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("콘서트 생성 요청시 201이 리턴된다.")
    @Test
    @WithMockUser
    void create() throws Exception {
        LocalDateTime concertDate = LocalDateTime.parse("2023-12-15T10:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-10-15T10:00:00");
        LocalDateTime ticketEndTime = LocalDateTime.parse("2023-10-17T10:00:00");

        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        TicketGradeDto ticketGradeDto = new TicketGradeDto("VIP", 100);
        ticketGradeDtoList.add(ticketGradeDto);

        ConcertRequestDto dto = new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, ticketGradeDtoList);

        List<ConcertRequestDto> concertRequestDtoList = new ArrayList<>();
        concertRequestDtoList.add(dto);

        String jsonBody = objectMapper.writeValueAsString(concertRequestDtoList);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/concert")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(csrf())
            .content(jsonBody));
        perform.andExpect(status().isCreated());
    }


}