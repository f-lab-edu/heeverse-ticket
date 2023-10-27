package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.service.reader.producer.TaskMessage;
import com.heeverse.ticket_order.service.reader.firstclass.GradeInfo;
import com.heeverse.ticket_order.service.reader.firstclass.ResultMap;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateSubscriber implements Subscriber<List<SimpleResponse>> {

    @ThreadSafe
    private final ResultMap result = new ResultMap();
    private final Set<String> uuidSet = new HashSet<>();
    private final ResultDBTransfer resultDBTransfer;


    @Override
    public void subscribe(TaskMessage<List<SimpleResponse>> taskMessage) {
        log.info("sub ");

        addUuid(taskMessage);
        GradeInfo gradeInfo = taskMessage.gradeInfo();

        Map<String, Long> collect = taskMessage.task().stream()
                .collect(Collectors.groupingBy(res -> gradeInfo.getGrade(res.ticketSeq()), Collectors.counting()));

        collect.entrySet().forEach(result::add);

        if(doneTask(taskMessage.totalCount())) {
            resultDBTransfer.transferAll(result.toList(taskMessage));
        }
    }

    private void addUuid(TaskMessage<List<SimpleResponse>> taskMessage) {
        uuidSet.add(taskMessage.taskUuid());
    }


    private boolean doneTask(int totalCount) {
        return totalCount == uuidSet.size();
    }

}
