package com.heeverse.common.exception;

import ch.qos.logback.classic.Logger;
import com.heeverse.member.exception.DuplicatedMemberException;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.LockOccupancyFailureException;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final int UNDEFINED_STATUS = -1;
    private final Logger log = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
    })
    public ResponseEntity<?> validationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }


    @ExceptionHandler({
            TicketAggregationFailException.class
    })
    public ResponseEntity<?> badRequestException(TicketAggregationFailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.BAD_REQUEST)));
    }


    @ExceptionHandler({
            AuthenticationServiceException.class
    })
    public ResponseEntity<?> authException(AuthenticationServiceException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.UNAUTHORIZED)));
    }


    @ExceptionHandler({
            DuplicatedMemberException.class,
            DuplicatedTicketException.class,
            AlreadyBookedTicketException.class,
            TicketingFailException.class
    })
    public ResponseEntity<?> duplicatedException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.CONFLICT)));
    }


    @ExceptionHandler(LockOccupancyFailureException.class)
    public ResponseEntity<?> serverException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> undefinedException(RuntimeException e) {
        log.warn("REST API 규약에 정의되지 않은 예외 {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.resolve(UNDEFINED_STATUS))));
    }


}
