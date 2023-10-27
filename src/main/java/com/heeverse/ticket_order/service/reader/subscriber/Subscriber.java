package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.service.reader.producer.TaskMessage;

public interface Subscriber<E> {
    void subscribe(TaskMessage<E> taskMessage);
}
