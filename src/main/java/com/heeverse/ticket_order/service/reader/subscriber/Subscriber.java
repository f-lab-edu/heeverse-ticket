package com.heeverse.ticket_order.service.reader.subscriber;

import com.heeverse.ticket_order.service.reader.firstclass.ResultConcurrentMap;

public interface Subscriber<E> {
    ResultConcurrentMap subscribe(TaskMessage<E> taskMessage);
}
