package com.heeverse.common.factory;

import ch.qos.logback.classic.Logger;
import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.member.domain.entity.Member;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import org.junit.jupiter.api.Assertions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class TicketLogFactory {

    @Autowired
    private TicketFactory ticketFactory;
    @Autowired
    private ConcertFactory concertFactory;
    @Autowired
    private MemberFactory memberFactory;
    @Autowired
    private TicketOrderFacade orderFacade;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Logger log = (Logger) LoggerFactory.getLogger(TicketLogFactory.class);
    @Autowired
    private TicketOrderAggregationMapper ticketOrderLogMapper;




    public TicketOrderingDto givenTicketOrder() {
        // member
        final long createdMemberSeq = givenMember(MemberTestHelper.getMockMember());
        // concertSeq
        final long concertSeq = givenConcert();
        // ticket grade
        final List<GradeTicket> gradeTickets = givenGradeTickets(TicketTestHelper.buildTicketGrade(concertSeq));
        // ticket
        final List<Ticket> tickets
                = gradeTickets.stream().map(grade -> TicketTestHelper.buildTicket(concertSeq, grade)).collect(Collectors.toList());

        givenTickets(concertSeq, tickets);

        final List<Ticket> createdTicket = selectTicketsByConcertSeq(concertSeq);

        return new TicketOrderingDto(createdMemberSeq, concertSeq, gradeTickets, createdTicket);

    }

    public Long givenMember(Member member) {

        Long memberSeq = memberFactory.createMember(member);
        Assertions.assertNotNull(memberSeq);
        return memberSeq;
    }

    public Long givenConcert() {
        // concert
        Concert concert = ConcertHelper.buildConcert();
        concertFactory.registerConcert(concert);
        Long concertSeq = concertFactory.selectLatestConcertSeq();

        Assertions.assertNotNull(concertSeq);
        return concertSeq;
    }


    public List<GradeTicket> givenGradeTickets(List<GradeTicket> gradeTickets) {
        // ticketGrade
        ticketFactory.insertTicketGrade(gradeTickets);
        return gradeTickets;
    }


    public long givenTickets(long concertSeq, List<Ticket> ticketList) {
        // ticket
        ticketFactory.insertTicket(ticketList);
        return concertSeq;
    }


    public List<Ticket> selectTicketsByConcertSeq(long concertSeq) {
        return ticketFactory.selectTicketSeqList(concertSeq);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void whenStartTicketOrder(List<Long> toOrderTicketSeqs, long createdMemberSeq) {
        orderFacade.startTicketOrderJob(new TicketOrderRequestDto(toOrderTicketSeqs), createdMemberSeq);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void afterTestDeleteData(TicketOrderingDto dto) {

        log.info("DELETE : concertSeq [{}], memberSeq [{}]", dto.getConcertSeq(), dto.getMemberSeq());

        jdbcTemplate.update(CONCERT_DELETE, dto.getConcertSeq());
        jdbcTemplate.update(GRADET_ICKET_DELETE, dto.getConcertSeq());
        jdbcTemplate.update(TICKET_ORDER_LOG_DELETE, dto.getConcertSeq());
        jdbcTemplate.update(TICKET_DELETE, dto.getConcertSeq());

        jdbcTemplate.update(MEMBER_DELETE, dto.getMemberSeq());
        jdbcTemplate.update(TICKET_ORDER_DELETE, dto.getMemberSeq());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<AggregateSelectMapperDto.SimpleResponse> selectLog(List<Long> whereIn) {
        return ticketOrderLogMapper.selectTicketSeqWhereIn(whereIn);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<AggregateSelectMapperDto.Response> selectAggregateResult(long concertSeq) {
        return jdbcTemplate.query(TICKET_ORDER_AGGR_SELECT, new Integer[]{Math.toIntExact(concertSeq)}, (rs, rowNum) -> new AggregateSelectMapperDto.Response(
                rs.getLong(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getInt(4)));
    }

    private static final String CONCERT_DELETE = "DELETE FROM concert where seq = ?";
    private static final String TICKET_DELETE = "DELETE FROM ticket where concert_seq = ?";
    private static final String TICKET_ORDER_LOG_DELETE = "DELETE FROM ticket_order_log_denormalization where concert_seq = ?";
    private static final String GRADET_ICKET_DELETE = "DELETE FROM grade_ticket where concert_seq = ?";
    private static final String MEMBER_DELETE = "DELETE FROM member where seq = ?";
    private static final String TICKET_ORDER_DELETE = "DELETE FROM ticket_order where member_seq = ?";
    private static final String TICKET_ORDER_AGGR_SELECT = "SELECT * FROM ticket_order_result WHERE concert_seq = ?";

}
