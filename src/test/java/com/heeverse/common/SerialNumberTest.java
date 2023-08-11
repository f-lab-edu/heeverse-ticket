package com.heeverse.common;


import com.heeverse.common.exception.SerialNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class SerialNumberTest {

    @Test
    @DisplayName("티켓 시리얼 넘버는 [공연일]-[공연시퀀스]-[티켓등급명]-[티켓 등급에 할당된 장수, 시리얼번호]")
    void ticket_serial_number_success() throws Exception {

        SerialTokenDto serialTokenDto = new TicketSerialTokenDto(
                LocalDate.of(2022, 1, 2),
                1L,
                "VIP석",
                1,
                5
        );

        TicketSerialNumber ticketSerialNumber = new TicketSerialNumber();
        String serialize = ticketSerialNumber.generate(serialTokenDto);

        Assertions.assertEquals(serialize, "20220102-1-VIP석-00001");
    }

    @Test
    @DisplayName("티켓 시리얼 넘버 생성 실패")
    void ticket_serial_number_failed() throws Exception {

        SerialTokenDto serialTokenDto = new TicketSerialTokenDto(
                null,
                1L,
                "VIP석",
                1,
                5
        );


        Assertions.assertThrowsExactly(
                SerialNumberException.class,
                () -> new TicketSerialNumber().generate(serialTokenDto)
        );
    }



}