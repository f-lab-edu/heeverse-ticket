package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.service.reader.StreamHelper;
import com.heeverse.ticket_order.service.reader.firstclass.ResultConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/26
 */
@Slf4j
public class AggregateSubscriber implements Subscriber<List<SimpleResponse>> {

    @ThreadSafe
    private final ResultConcurrentMap resultConcurrentMap = new ResultConcurrentMap(new ConcurrentHashMap<>());

    public ResultConcurrentMap subscribe(
            TaskMessage<List<SimpleResponse>> taskMessage
    ) {

        taskMessage.task()
                .stream()
                .collect(StreamHelper.countGroupingByKey(taskMessage.gradeInfo()))
                .entrySet()
                .forEach(resultConcurrentMap::add);

        return resultConcurrentMap;
    }

}
