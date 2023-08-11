package com.heeverse.common;

import lombok.Getter;

import java.time.LocalDate;

/**
 * @author gutenlee
 * @since 2023/08/11
 */
@Getter
public class TicketSerialTokenDto extends SerialTokenDto {

    private final static Delimiter defaultDelimiter = Delimiter.DASH;

    private final LocalDate concertDate;
    private final long concertSeq;
    private final String gradeName;
    private final int index;
    private final int indexLength;


    public TicketSerialTokenDto(LocalDate concertDate, long concertSeq, String gradeName, int index, int indexLength) {
        super(defaultDelimiter);
        this.concertDate = concertDate;
        this.concertSeq = concertSeq;
        this.gradeName = gradeName;
        this.index = index;
        this.indexLength = indexLength;
    }
}
