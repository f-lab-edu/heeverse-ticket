package com.heeverse.ticket.domain;

import com.heeverse.common.SerialNumber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static com.heeverse.common.util.StringUtils.leftPad;

/**
 * @author gutenlee
 * @since 2023/08/10
 */
public class TicketSerialNumber implements SerialNumber<String> {

    private final static char ZERO = '0';
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String serialNumber;

    public TicketSerialNumber(TicketSerialTokenDto tokenDto) {
        this.serialNumber = generate(tokenDto);
    }

    public String getSerial() {
        return serialNumber;
    }

    private String generate(TicketSerialTokenDto tokenDto) {

            StringJoiner sj = new StringJoiner(tokenDto.getDelimiter().getValue());
            sj.add(dateToString(tokenDto.getConcertDate()));
            sj.add(tokenDto.getConcertSeqAsLong());
            sj.add(tokenDto.getGradeName());
            sj.add(addPad(tokenDto.getIndexAsString(), tokenDto.getTicketCountStrLength()));

            return sj.toString();
    }


    private String dateToString(LocalDateTime date) {
        return df.format(date);
    }


    private String addPad(String str, int size) {
        return leftPad(str, size, ZERO);
    }

}
