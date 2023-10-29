package com.heeverse.ticket_order.service.reader.producer;

import com.heeverse.ticket_order.service.reader.subscriber.Subscriber;
import com.heeverse.ticket_order.service.transfer.ResultDBTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto.SimpleResponse;

/**
 * @author gutenlee
 * @since 2023/10/27
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TaskPublisher implements Publisher<List<SimpleResponse>> {

    private final ResultDBTransfer transfer;

    public void publish(
            TaskMessage<List<SimpleResponse>> task,
            Subscriber<List<SimpleResponse>> subscriber
    ) {
        log.info("pub!!");
        subscriber.subscribe(task, transfer);
    }


    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

}
