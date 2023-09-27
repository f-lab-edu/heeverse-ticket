package com.heeverse.concert.controller;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jeongheekim
 * @date 2023/08/25
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "local")
public class ConcertIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ConcertMapper concertMapper;

    @BeforeEach
    void setUp() {
        LocalDateTime concertDate = LocalDateTime.parse("2023-12-15T10:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-10-15T10:00:00");
        LocalDateTime ticketEndTime = LocalDateTime.parse("2023-10-17T10:00:00");

        for (int i = 1; i <= 10; i++) {
            ConcertRequestDto dto = new ConcertRequestDto("BTS콘서트 - " + i, concertDate,
                ticketOpenTime, ticketEndTime, 1L + i, 1L + i, null);
            Concert concert = new Concert(dto);
            concertMapper.insertConcert(concert);
        }
    }


    @Test
    @DisplayName("size 개수만큼 콘서트 목록을 조회한다.")
    void getAllConcertListTest() throws Exception {
        String concertName = "BTS";
        int page = 0;
        int size = 3;

        ResultActions resultActions = mockMvc.perform(get("/concert")
            .param("concertName", concertName)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)));

        resultActions.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.*", hasSize(size)));
    }

}
