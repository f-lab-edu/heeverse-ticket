package com.heeverse.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.common.exception.ErrorMessage;
import com.heeverse.common.factory.WithMockMember;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.exception.DuplicatedMemberException;
import com.heeverse.member.service.MemberService;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket_order.domain.dto.TicketRemainsDto;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import com.heeverse.ticket_order.service.QueryAggregationService;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import com.heeverse.ticket_order.service.TicketOrderTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.heeverse.ControllerTestHelper.Endpoint;
import static com.heeverse.ControllerTestHelper.getRestDocsMockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gutenlee
 * @since 2023/10/06
 */
@WebMvcTestForRestDocs
public class ControllerExceptionUnitTest {

    private final String ERROR_RESPONSE_FIELD_NAME = "message";

    @MockBean
    private MemberService memberService;
    @MockBean
    private ConcertService concertService;
    @MockBean
    private TicketOrderFacade ticketOrderFacade;
    @MockBean
    private QueryAggregationService aggregationService;

    @Autowired
    private ObjectMapper om;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp(@Autowired RestDocumentationContextProvider restDocumentation) {
        mockMvc = getRestDocsMockMvc(wac, restDocumentation);
    }



    @Test
    @DisplayName("/member POST, 예외 응답 Body 테스트")
    void memberException() throws Exception {

        final MemberRequestDto mockDto = MemberTestHelper.getMockMemberRequestDto();

        when(memberService.signup(Mockito.any())).
                thenThrow(DuplicatedMemberException.class);


        mockMvc.perform(post(Endpoint.Member.회원가입)
                        .content(om.writeValueAsString(mockDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().isConflict())
                .andExpect(jsonPath(ERROR_RESPONSE_FIELD_NAME).value(ErrorMessage.CLIENT_ERROR.message))
                .andDo(MemberDocsResultFactory.memberErrorDocs());

    }


    @Test
    @DisplayName("/concert, POST 예외 응답 Body 테스트")
    void concertExceptionTest() throws Exception {

        when(concertService.registerConcert(Mockito.any()))
                .thenThrow(DuplicatedTicketException.class);

        mockMvc.perform(post(Endpoint.CONCERT.콘서트_등록)
                        .content(om.writeValueAsString(List.of(ConcertHelper.normalDto())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().isConflict())
                .andExpect(jsonPath(ERROR_RESPONSE_FIELD_NAME).value(ErrorMessage.CLIENT_ERROR.message))
                .andDo(ConcertDocsResultFactory.concertErrorDocs());
    }


    @Test
    @DisplayName("/ticket-order, POST 예외 응답 Body 테스트")
    @WithMockMember
    void ticketOrderExceptionTest() throws Exception {

        when(ticketOrderFacade.startTicketOrderJob(Mockito.any(), Mockito.anyLong()))
                .thenThrow(TicketingFailException.class);

        mockMvc.perform(post(Endpoint.TICKET.티켓_예매)
                        .content(om.writeValueAsString(TicketOrderTestHelper.createTicketOrderRequestDto(List.of(1L))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().isBadRequest())
                .andExpect(jsonPath(ERROR_RESPONSE_FIELD_NAME).value(ErrorMessage.CLIENT_ERROR.message))
                .andDo(TicketDocsResultFactory.ticketOrderErrorDocs());

    }


    @Test
    @DisplayName("/ticker-order/remains, GET 예외 응답 Body 테스트")
    void ticketRemainsExceptionTest() throws Exception {

        when(ticketOrderFacade.getTicketRemains(Mockito.any()))
                .thenThrow(TicketAggregationFailException.class);

        mockMvc.perform(get(Endpoint.TICKET.잔여_티켓_집계)
                        .content(om.writeValueAsString(new TicketRemainsDto(1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().isBadRequest())
                .andExpect(jsonPath(ERROR_RESPONSE_FIELD_NAME).value(ErrorMessage.CLIENT_ERROR.message))
                .andDo(TicketDocsResultFactory.tickerRemainsErrorDocs());
    }


    @Test
    @DisplayName("정의되지 않은 RuntimeException 경우 에러 메세지")
    void undefinedExceptionTest() throws Exception {

        when(ticketOrderFacade.getTicketRemains(Mockito.any()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(get(Endpoint.TICKET.잔여_티켓_집계)
                        .content(om.writeValueAsString(new TicketRemainsDto(1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().isBadRequest())
                .andExpect(jsonPath(ERROR_RESPONSE_FIELD_NAME).value(ErrorMessage.UNDEFINED_ERROR.message));
    }


}
