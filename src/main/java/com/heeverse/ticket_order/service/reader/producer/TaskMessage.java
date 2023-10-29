package com.heeverse.ticket_order.service.reader.producer;

import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;

/**
 * @author gutenlee
 * @since 2023/10/27
 */
public record TaskMessage<T>(
        String taskUuid,
        long concertSeq,
        int totalCount,
        T task,
        GradeInfo gradeInfo
) {

    @Override
    public String toString() {
        return "TaskMessage{" +
                "taskUuid='" + taskUuid + '\'' +
                ", totalCount=" + totalCount +
                ", task=" + task +
                '}';
    }
}
