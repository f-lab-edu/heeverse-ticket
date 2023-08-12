package com.heeverse.ticket.domain.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.TicketGrade;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketMapperTest {

    private static final Long CONCERT_ID = 1L;

    @Autowired
    private TicketMapper ticketMapper;


    @Test
    @DisplayName("[티켓 등급 생성] INSERT 성공하면 조회 결과 개수와 동일해야 한다")
    List<TicketGrade> 티켓_등급_생성() throws Exception {

        List<TicketGrade> ticketGrades = TicketTestHelper.toTicketGrade(TicketTestHelper.createTicketCategoryDtos(), CONCERT_ID);

        ticketMapper.insertTicketGrade(ticketGrades);

        List<TicketGrade> saved = ticketMapper.findTicketGrades(CONCERT_ID);
        assertThat(saved, hasSize(ticketGrades.size()));

        return saved;
    }


    @Test
    @DisplayName("[티켓 생성] 생성한 티켓 객체와 저장된 티켓 개수 일치")
    void 티켓_생성() throws Exception {

        final var ticketGrades = 티켓_등급_생성();

        List<Ticket> tickets = TicketTestHelper.publishTicket(ticketGrades);
        ticketMapper.insertTicket(tickets);


        assertThat(ticketMapper.findTickets(CONCERT_ID), hasSize(tickets.size()));
    }


    @Test
    @DisplayName("[티켓 조회] 티켓 조회 결과의 리스트 타입 파라미는 Ticket.class")
    void readTicketTest() throws Exception {

        티켓_생성();

        List<Ticket> tickets = ticketMapper.findTickets(CONCERT_ID);

        assertThat(tickets.get(0), instanceOf(Ticket.class));
    }




}