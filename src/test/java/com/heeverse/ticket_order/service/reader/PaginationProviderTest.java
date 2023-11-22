package com.heeverse.ticket_order.service.reader;

import com.heeverse.common.util.PaginationProvider;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/27
 */

@SpringBootTest
@ActiveProfiles("dev-test")
public class PaginationProviderTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TicketMapper ticketMapper;

    private List<Ticket> tickets;
    private final Logger log = LoggerFactory.getLogger(PaginationProviderTest.class);

    @BeforeEach
    void setUp() {
        tickets = ticketMapper.findTickets(1L);
    }

    @Test
    @DisplayName("리스트를 chuckSize 만큼 sublist로 분할하면 개수는 (list pageSize / pageSize) 결과의 올림한 값과 같아야 한다")
    void chunkTest() {

        // given
        final int chuckSize = 3;
        final List<Long> list = LongStream.rangeClosed(1, 7).boxed().toList();

        // when
        List<List<Long>> chunk = PaginationProvider.toChunk(list, chuckSize);

        // then
        Assertions.assertEquals(Math.ceil((double) list.size() / chuckSize), chunk.size());
    }


    @Nested
    @Disabled
    class PagingTest {

        final static String OFFSET_QUERY = "SELECT ticket_seq, seq  FROM ticket_order_log_copy WHERE concert_seq = 1 ORDER BY concert_seq, seq LIMIT ? OFFSET ?";
        final static String WHERE_IN_QUERY = "SELECT ticket_seq, seq FROM ticket_order_log_copy WHERE concert_seq = 1 AND ticket_seq IN (:ids)";
        final static String NO_OFFSET_QUERY = "SELECT ticket_seq, seq FROM ticket_order_log_copy WHERE concert_seq = 1 AND seq > ? ORDER BY concert_seq, seq LIMIT 0, ?";
        final static String CURSOR_QUERY = "SELECT ticket_seq, seq FROM ticket_order_log_copy WHERE concert_seq = 1 AND seq > ? ORDER BY concert_seq, seq LIMIT ?";
        final static String KEYSET_QUERY = "SELECT ticket_seq, seq FROM ticket_order_log_copy WHERE (concert_seq, seq) > (?, ?) LIMIT ?";
        final static int SIZE = 10_000;
        final static int FETCH_SIZE = 1_000_000;

        @Test
        @Disabled
        @DisplayName("CURSOR 쿼리")
        void cursorTest() throws Exception {
            // 26
            log.info("CURSOR 쿼리 시작 시간 {}", LocalDateTime.now());
            int count = 0;
            int seq = 0;

            do {
                List<SimpleResponse> results
                        = jdbcTemplate.query(CURSOR_QUERY, new Integer[]{seq, SIZE}, getSimpleResponseRowMapper());

                seq = (int) results.get(results.size() - 1).seq();
                log.info("seq {}", seq);

                count += results.size();
            } while (count < FETCH_SIZE);

            log.info("CURSOR 쿼리 종료 시간 {}", LocalDateTime.now());
        }

        @Test
        @DisplayName("NO OFFSET 쿼리")
        void noOffsetTest() throws Exception {
            // 1 min 27 sec
            log.info("NO OFFSET 쿼리 시작 시간 {}", LocalDateTime.now());
            int seq = 0;
            int count = 0;
            do {
                List<SimpleResponse> results
                        = jdbcTemplate.query(NO_OFFSET_QUERY, new Integer[]{seq, SIZE}, getSimpleResponseRowMapper());
                seq = (int) results.get(results.size() - 1).seq();
                log.info("last seq {}",  results.get(results.size() - 1).seq());
                log.info("count {}", count);

                count += results.size();
            } while (count < FETCH_SIZE);

            log.info("NO OFFSET 쿼리 종료 시간 {}", LocalDateTime.now());
        }


        @Test
//        @Disabled
        @DisplayName("OFFSET 쿼리")
        void offsetTest() {
            // over 2m
            log.info("OFFSET 쿼리 시작 시간 {}", LocalDateTime.now());

            int count = 0;
            int i = 0;
            do {
                int offset = (i + 1) * SIZE;
                List<SimpleResponse> results
                        = jdbcTemplate.query(OFFSET_QUERY, new Integer[]{SIZE, offset}, getSimpleResponseRowMapper());

                i++;
                count += results.size();
                log.info("count {}", count);
            } while (count < FETCH_SIZE);

            log.info("OFFSET 쿼리 종료 시간 {}", LocalDateTime.now());
        }

        @Test
        @DisplayName("WHERE IN 쿼리")
        void whereInTest() {
            // 3M 28S
            log.info("WHERE IN 쿼리 시작 시간 {}", LocalDateTime.now());

            List<Long> list = tickets.stream().map(Ticket::getSeq).collect(Collectors.toList());
            List<List<Long>> chunk = PaginationProvider.toChunk(list, 10);

            int count = 0;
            for (List<Long> page : chunk) {
                List<SimpleResponse> results = jdbcTemplate.query(WHERE_IN_QUERY.replace(":ids", generateQMarkString(page.size())),
                        page.toArray(new Object[0]),
                        getSimpleResponseRowMapper());

                log.info("results {}", results.size());
                count += results.size();
                if (count > FETCH_SIZE) {
                    break;
                }
            }


            log.info("WHERE IN 쿼리 종료 시간 {}", LocalDateTime.now());
        }


        @Test
        @DisplayName("KEY SET 페이징 쿼리")
        void keysetTest() throws Exception {
            log.info("KEY SET 쿼리 시작 시간 {}", LocalDateTime.now());

            //concert_seq, ticket_seq, seq

            int count = 0;
            long concertSeq = 1;
            long seq = 1;

            do {
                List<SimpleResponse> results
                        = jdbcTemplate.query(KEYSET_QUERY, new Long[]{concertSeq, seq, (long) SIZE}, getSimpleResponseRowMapper());

                seq = results.get(results.size() - 1).seq();

                count += results.size();
                log.info("count {}", count);
            } while (count < FETCH_SIZE);

            log.info("KEY SET 쿼리 종료 시간 {}", LocalDateTime.now());
        }

        private String generateQMarkString(int size) {
            if (size == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder("?");
            for (int i = 1; i < size; i++) {
                sb.append(", ?");
            }
            return sb.toString();
        }
    }

    private static RowMapper<SimpleResponse> getSimpleResponseRowMapper() {
        return (rs, rowNum) -> {
            int seq = rs.getInt(2);
            int ticketSeq = rs.getInt(1);
            return new SimpleResponse(seq, ticketSeq);
        };
    }

}
