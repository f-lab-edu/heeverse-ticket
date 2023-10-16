package com.heeverse.ticket_order.service.transfer;

import java.util.List;

public interface ResultTransfer<T> {
    List<T> transferAll(List<T> collections);
}
