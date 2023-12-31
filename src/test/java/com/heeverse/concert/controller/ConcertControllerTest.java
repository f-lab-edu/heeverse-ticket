package com.heeverse.concert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.ticket.dto.TicketGradeDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        TicketGradeDto ticketGradeDto = new TicketGradeDto("VIP", 100);
        ticketGradeDtoList.add(ticketGradeDto);

        ConcertRequestDto dto = ConcertHelper.normalDto();

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