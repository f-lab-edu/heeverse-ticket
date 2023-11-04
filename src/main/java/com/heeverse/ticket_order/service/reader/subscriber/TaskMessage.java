package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;

/**
 * @author gutenlee
 * @since 2023/10/27
 */
public record TaskMessage<T>(
        long concertSeq,
        T task,
        GradeInfo gradeInfo
) {


    @Override
    public String toString() {
        return "TaskMessage{" +
                "concertSeq=" + concertSeq +
                ", task=" + task +
                ", gradeInfo=" + gradeInfo +
                '}';
    }
}
