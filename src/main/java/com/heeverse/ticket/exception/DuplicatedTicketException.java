package com.heeverse.ticket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedTicketException extends RuntimeException {

    private static final String MESSAGE = "이미 등록된 티켓이 존재합니다";

    public DuplicatedTicketException() {
        super(MESSAGE);
    }

}
