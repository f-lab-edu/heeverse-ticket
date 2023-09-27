package com.heeverse.common;


import com.heeverse.common.util.StringUtils;
import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.dto.TicketGradeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static com.heeverse.common.AssertUtils.assertThrowNPE;
import static org.junit.jupiter.api.Assertions.assertAll;

class SerialNumberTest {

    private static GradeTicket getGradeTicket(String gradeName, int seatCount) {
        TicketGradeDto ticketGradeDto = new TicketGradeDto( gradeName, seatCount);
        return new GradeTicket(ticketGradeDto, 0);
    }

    private static TicketSerialTokenDto getTicketSerialTokenDto(LocalDateTime concertDate,
        long concertSeq, GradeTicket grade, int idx) {
        return new TicketSerialTokenDto(concertDate, concertSeq, grade, idx);
    }

    @Test
    @DisplayName("ticketSerialTokenDto 생성자 nonNull 테스트")
    void ticketSerialTokenDto_nonNull_test() throws Exception {
        assertAll(
            () -> assertThrowNPE(() -> getTicketSerialTokenDto(null, 1L, Mockito.mock(GradeTicket.class), 1)),
            () -> assertThrowNPE(
                () -> getTicketSerialTokenDto(Mockito.mock(LocalDateTime.class), 1L, null, 1))
        );
    }


    @Test
    @DisplayName("티켓 생성 인덱스에 패드 추가 텍스트")
    void addPadTest() throws Exception {
        assertAll(
            () -> assertIndexWithPad(1, "0001"),
            () -> assertIndexWithPad(10, "0010"),
            () -> assertIndexWithPad(100, "0100"),
            () -> assertIndexWithPad(1000, "1000"),
            () -> assertIndexWithPad(1111, "1111")
        );
    }

    private void assertIndexWithPad(int idx, String expected){

        TicketSerialTokenDto serialTokenDto = getTicketSerialTokenDto(
            Mockito.mock(LocalDateTime.class),
            1L,
            getGradeTicket("VIP", 1000),
            idx
        );

        Assertions.assertEquals(expected, StringUtils.leftPad(serialTokenDto.getIndexAsString(), serialTokenDto.getTicketCountStrLength(), '0'));
    }


    @Test
    @DisplayName("티켓 시리얼 넘버는 [공연일]-[공연시퀀스]-[티켓등급명]-[티켓 등급에 할당된 장수, 시리얼번호]")
    void ticketSerialNumberSuccess() throws Exception {

        TicketSerialTokenDto serialTokenDto = getTicketSerialTokenDto(
            LocalDateTime.of(2022, 1, 2, 9, 10, 30),
            1L,
            getGradeTicket("VIP", 1000),
            1
        );

        Assertions.assertEquals("20220102-1-VIP-0001", new TicketSerialNumber(serialTokenDto).getSerial());
    }

}
