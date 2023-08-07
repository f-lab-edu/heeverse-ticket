package com.heeverse.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
public class DateAdapter implements DateTarget {

    private final LocalDateTime localDateTime;


    public DateAdapter(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public Date toDate() {
        return Date.from(this.localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
