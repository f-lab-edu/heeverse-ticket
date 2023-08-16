package com.heeverse.common;


import com.heeverse.common.exception.SerialNumberException;
import com.heeverse.common.util.StringUtils;
import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.dto.TicketGradeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;

class SerialNumberTest {


    private GradeTicket getGradeTicket(String gradeName, int seatCount) {
        TicketGradeDto ticketGradeDto = new TicketGradeDto(1, gradeName, seatCount);
        return new GradeTicket(ticketGradeDto, 0);
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

        TicketSerialTokenDto serialTokenDto = new TicketSerialTokenDto(
                LocalDate.of(2022, 1, 2),
                1L,
                getGradeTicket("VIP", 1000),
                idx
        );

        Assertions.assertEquals(expected, StringUtils.leftPad(serialTokenDto.getIndexAsString(), serialTokenDto.getTicketCountStrLength(), '0'));
    }


    @Test
    @DisplayName("티켓 시리얼 넘버는 [공연일]-[공연시퀀스]-[티켓등급명]-[티켓 등급에 할당된 장수, 시리얼번호]")
    void ticket_serial_number_success() throws Exception {

        TicketGradeDto ticketGradeDto = new TicketGradeDto(1, "VIP", 1000);
        GradeTicket gradeTicket = new GradeTicket(ticketGradeDto, 1L);

        TicketSerialTokenDto serialTokenDto = new TicketSerialTokenDto(
                LocalDate.of(2022, 1, 2),
                1L,
                gradeTicket,
                1
        );

        TicketSerialNumber ticketSerialNumber = new TicketSerialNumber();
        String serialize = ticketSerialNumber.generate(serialTokenDto);

        Assertions.assertEquals("20220102-1-VIP-0001", serialize);
    }

    @Test
    @DisplayName("티켓 시리얼 넘버 생성 실패")
    void ticket_serial_number_failed() throws Exception {

        TicketGradeDto ticketGradeDto = new TicketGradeDto(1, "VIP", 1000);
        GradeTicket gradeTicket = new GradeTicket(ticketGradeDto, 1L);

        TicketSerialTokenDto serialTokenDto = new TicketSerialTokenDto(
                null,
                1L,
                gradeTicket,
                1
        );


        Assertions.assertThrowsExactly(
                SerialNumberException.class,
                () -> new TicketSerialNumber().generate(serialTokenDto)
        );
    }



}