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
import com.heeverse.ticket_order.domain.dto.*;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketOrderRequestMapperDto;
import com.heeverse.ticket_order.domain.dto.persistence.TicketRemainsResponseMapperDto;
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

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.heeverse.ControllerTestHelper.getRestDocsMockMvc;
import static com.heeverse.ticket_order.domain.dto.enums.StrategyType.QUERY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gutenlee
 * @since 2023/10/08
 */

@WebMvcTestForRestDocs
public class ControllerResponseUnitTest {

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
    private static final Random random = ThreadLocalRandom.current();


    @BeforeEach
    void setUp(@Autowired RestDocumentationContextProvider restDocumentation) {
        mockMvc = getRestDocsMockMvc(wac, restDocumentation);
    }

    @Test
    @DisplayName("/member POST, 정상 응답 Body 테스트")
    void memberResponseTest() throws Exception {

        final MemberRequestDto mockDto = MemberTestHelper.getMockMemberRequestDto();

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
    void concertResponseTest() throws Exception {

        // given
        final List<Long> givenConcertSeq = List.of(1L);

        when(concertService.registerConcert(Mockito.any())).thenReturn(givenConcertSeq);

        when(concertService.getRegisteredConcertList(givenConcertSeq))
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
    void ticketOrderResponseTest() throws Exception {

        // given
        final long concertSeq = 1L;
        final TicketOrderRequestDto orderRequestDtos = givenTicketOrderRequest(concertSeq);
        final List<TicketOrderResponseDto> orderResponseDtos = givenTicketOrderResponse(concertSeq);

        // when
        when(ticketOrderFacade.startTicketOrderJob(Mockito.any(), Mockito.any()))
                .thenReturn(orderResponseDtos);

        // then
        mockMvc.perform(post(ControllerTestHelper.Endpoint.TICKET.티켓_예매)
                        .content(om.writeValueAsString(orderRequestDtos))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(res -> status().isCreated().match(res))
                .andDo(TicketDocsResultFactory.tickerOrderSuccessDocs());

    }


    @Test
    @DisplayName("/ticker-order/remains, GET 정상 응답 Body 테스트")
    void ticketRemainsResponseTest() throws Exception {
        // given
        final long concertSeq = 1L;
        final List<TicketRemainsResponseDto> ticketRemainsResponseDtos = givenTicketRemainsResponse(concertSeq);

        // when
        when(ticketOrderFacade.getTicketRemains(Mockito.any()))
                .thenReturn(ticketRemainsResponseDtos);

        // then
        mockMvc.perform(get(ControllerTestHelper.Endpoint.TICKET.잔여_티켓_집계)
                        .content(om.writeValueAsString(new TicketRemainsDto(concertSeq)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(res -> status().is2xxSuccessful().match(res))
                .andDo(TicketDocsResultFactory.tickerRemainsResponseDocs());
    }


    @Test
    void ticketOrderAggregationTest() throws Exception {
        final long concertSeq = 1;
        AggregateDto.Response response = new AggregateDto.Response(new AggregateSelectMapperDto.Response(
                concertSeq,
                "VIP",
                100,
                122_342
        ));

        when(aggregationService.aggregate(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(ControllerTestHelper.Endpoint.TICKET.티켓_예매_집계)
                        .content(om.writeValueAsString(new AggregateDto.Request(1L, true, QUERY)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(res -> status().is2xxSuccessful().match(res))
                .andDo(TicketDocsResultFactory.ticketOrderLogDocs());
    }



    private static List<Ticket> givenTickets(long concertSeq) {
        List<GradeTicket> gradeTickets = TicketTestHelper.buildTicketGrade(concertSeq);
        return gradeTickets.stream()
                .map(grade -> TicketTestHelper.buildTicket(concertSeq, grade))
                .map(ticket -> {
                    try {
                        Field seq = ticket.getClass().getDeclaredField("seq");
                        seq.setAccessible(true);
                        seq.set(ticket, random.nextLong(10));
                        return ticket;
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private static TicketOrderRequestDto givenTicketOrderRequest(long concertSeq) {
        List<Ticket> tickets = givenTickets(concertSeq);
        return TicketOrderTestHelper.createTicketOrderRequestDto(
                tickets.stream()
                    .map(Ticket::getSeq)
                    .collect(Collectors.toList())
        );
    }

    private static List<TicketOrderResponseDto> givenTicketOrderResponse(long concertSeq) {
        Concert concert = ConcertHelper.buildConcert();
        List<Ticket> tickets = givenTickets(concertSeq);
        return tickets.stream()
                .map(ticket -> new TicketOrderResponseDto(
                        new TicketOrderRequestMapperDto(
                                concert.getConcertName(),
                                concert.getConcertDate(),
                                ticket.getTicketSerialNumber(),
                                ticket.getGradeName(),
                                LocalDateTime.now(),
                                BookingStatus.SUCCESS)
                )).collect(Collectors.toList());
    }


    private List<TicketRemainsResponseDto> givenTicketRemainsResponse(long concertSeq) {
        List<Ticket> tickets = givenTickets(concertSeq);
        return tickets.stream()
                .map(ticket -> new TicketRemainsResponseDto(
                            new TicketRemainsResponseMapperDto(
                                    concertSeq,
                                    ticket.getGradeName(),
                                    random.nextInt(1,
                                            50_000)
                            )
                ))
                .collect(Collectors.toList());
    }

}
