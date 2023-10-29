package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.service.reader.producer.TaskMessage;
import com.heeverse.ticket_order.service.transfer.ResultTransfer;

public interface Subscriber<E> {
    void subscribe(TaskMessage<E> taskMessage, ResultTransfer<AggregateInsertMapperDto> transfer);
}
