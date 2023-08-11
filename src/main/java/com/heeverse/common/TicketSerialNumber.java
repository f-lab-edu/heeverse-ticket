package com.heeverse.common;

import com.heeverse.common.exception.SerialNumberException;

import java.time.LocalDate;
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



    public String generate(SerialTokenDto serialTokenDto) {

        TicketSerialTokenDto tokenDto = (TicketSerialTokenDto) serialTokenDto;

        StringJoiner sj = null;
        try {
            sj = new StringJoiner(tokenDto.delimiter.getValue());
            sj.add(dateToString(tokenDto.getConcertDate()))
                .add(Long.toString(tokenDto.getConcertSeq()))
                .add(tokenDto.getGradeName())
                .add(addPad(Integer.toString(tokenDto.getIndex()), tokenDto.getIndexLength()));
        } catch (NullPointerException e) {
            throw new SerialNumberException(e);
        }

        return sj.toString();
    }


    public String dateToString(LocalDate date) {
        return df.format(date);
    }


    public String addPad(String str, int size) {
        return leftPad(str, size, ZERO);
    }


}
