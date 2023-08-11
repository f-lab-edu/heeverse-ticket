package com.heeverse.concert.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.heeverse.concert.service.ConcertService;
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

    @DisplayName("JSON request가 dto에 직렬화된다.")
    @Test
    @WithMockUser
    void create() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/concert")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(csrf())
            .content("""
                [
                     {
                         "concertName": "BTS 콘서트",
                         "concertDate": "2023-12-15T10:00:00",
                         "ticketOpenTime": "2023-10-15T10:00:00",
                         "ticketEndTime": "2023-10-17T10:00:00",
                         "artistId": 1,
                         "venueId": 1,
                         "gradeTicketDtoList": [
                             {
                                 "gradeName": "VIP",
                                 "ticketCount": 100
                             },
                             {
                                 "gradeName": "R",
                                 "ticketCount": 200
                             }
                         ]
                     }
                 ]
                """));
        perform.andExpect(status().isCreated());
    }

    @DisplayName("티켓 오픈 시간은 티켓 종료 시간보다 미래이면 bad request를 반환한다.")
    @Test
    @WithMockUser
    void requestDto_Null_exception_test() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/concert")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(csrf())
            .content("""
                [
                      {
                          "concertName": "BTS 콘서트",
                          "concertDate": "2023-12-15T10:00:00",
                          "ticketOpenTime": "2023-10-18T10:00:00",
                          "ticketEndTime": "2023-10-17T10:00:00",
                          "artistId": 1,
                          "venueId": 1,
                          "gradeTicketDtoList": [
                              {
                                  "gradeName": "VIP",
                                  "ticketCount": 100
                              },
                              {
                                  "gradeName": "R",
                                  "ticketCount": 200
                              }
                          ]
                      }
                  ]
                """));

        perform.andExpect(status().isBadRequest());
    }
}