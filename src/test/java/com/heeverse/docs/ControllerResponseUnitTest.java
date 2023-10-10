package com.heeverse.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.ControllerTestHelper;
import com.heeverse.common.factory.WithMockMember;
import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.concert.service.ConcertService;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.enums.BookingStatus;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import com.heeverse.ticket_order.service.TicketOrderTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.heeverse.ControllerTestHelper.getRestDocsMockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gutenlee
 * @since 2023/10/08
 */

@WebMvcTest
@AutoConfigureRestDocs
public class ControllerResponseUnitTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private ConcertService concertService;
    @MockBean
    private TicketOrderFacade ticketOrderFacade;

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
    @DisplayName("/member POST, 정상 응답 Body 테스트")
    void memberException() throws Exception {

        MemberRequestDto mockDto = MemberTestHelper.mockingMemberRequestDto();

        when(memberService.signup(Mockito.any())).thenReturn(1L);


        mockMvc.perform(post(ControllerTestHelper.Endpoint.Member.회원가입)
                        .content(om.writeValueAsString(mockDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(res -> status().isCreated().match(res))
                .andDo(MemberDocsResultFactory.memberSuccessDocs());

    }


    @Test
    @DisplayName("/concert, POST 정상 응답 Body 테스트")
    void concertTest() throws Exception {

        when(concertService.registerConcert(Mockito.any())).thenReturn(List.of(1L));

        when(concertService.getRegisteredConcertList(List.of(1L)))
                .thenReturn(List.of(ConcertHelper.mockRegisteredConcertResponseDto()));

        mockMvc.perform(post(ControllerTestHelper.Endpoint.CONCERT.콘서트_등록)
                        .content(om.writeValueAsString(List.of(ConcertHelper.normalDto())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(res -> status().isCreated().match(res))
                .andDo(ConcertDocsResultFactory.concertSuccessDocs());
    }


    @Test
    @DisplayName("/ticket-order, POST 정상 응답 Body 테스트")
    @WithMockMember
    void ticketOrderTest() throws Exception {

        // 공연
        Concert concert = ConcertHelper.buildConcert();
        long concertSeq = 1L;
        // 티켓
        List<GradeTicket> gradeTickets = TicketTestHelper.buildTicketGrade(concertSeq);
        List<Ticket> tickets = gradeTickets.stream()
                .map(grade -> TicketTestHelper.buildTicket(concertSeq, grade))
                .toList();
        // 예매 요청
        TicketOrderRequestDto orderRequestDtos = TicketOrderTestHelper.createTicketOrderRequestDto(tickets.stream()
                .map(Ticket::getSeq)
                .collect(Collectors.toList()));

        List<TicketOrderResponseDto> orderResponseDtos = tickets.stream()
                .map(ticket -> new TicketOrderResponseDto(
                                    new TicketOrderRequestMapperDto(
                                            concert.getConcertName(),
                                            concert.getConcertDate(),
                                            ticket.getTicketSerialNumber(),
                                            ticket.getGradeName(),
                                            LocalDateTime.now(),
                                            BookingStatus.SUCCESS)
                )).collect(Collectors.toList());

        when(ticketOrderFacade.startTicketOrderJob(Mockito.any(), Mockito.any()))
                .thenReturn(orderResponseDtos);

        mockMvc.perform(post(ControllerTestHelper.Endpoint.TICKET.티켓_예매)
                        .content(om.writeValueAsString(orderRequestDtos))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(res -> status().isCreated().match(res))
                .andDo(TicketDocsResultFactory.tickerOrderSuccessDocs());

    }


}
