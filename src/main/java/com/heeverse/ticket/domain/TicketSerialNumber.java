package com.heeverse.ticket.domain;

import com.heeverse.common.SerialNumber;
import com.heeverse.common.exception.SerialNumberException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static com.heeverse.common.util.StringUtils.leftPad;

/**
 * @author gutenlee
 * @since 2023/08/10
 */
public class TicketSerialNumber implements SerialNumber<String, TicketSerialTokenDto> {

    private final static char ZERO = '0';
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");



    public String generate(TicketSerialTokenDto tokenDto) {

        try {
            StringJoiner sj = new StringJoiner(tokenDto.getDelimiter().getValue());
            sj.add(dateToString(tokenDto.getConcertDate()));
            sj.add(tokenDto.getConcertSeqAsLong());
            sj.add(tokenDto.getGradeName());
            sj.add(addPad(tokenDto.getIndexAsString(), tokenDto.getTicketCountStrLength()));

            return sj.toString();
        } catch (NullPointerException e) {
            throw new SerialNumberException(e);
        }

    }


    private String dateToString(LocalDate date) {
        return df.format(date);
    }


    private String addPad(String str, int size) {
        return leftPad(str, size, ZERO);
    }

}
