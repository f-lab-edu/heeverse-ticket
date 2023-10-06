package com.heeverse.common.exception;

import com.heeverse.member.exception.DuplicatedMemberException;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import com.heeverse.ticket_order.domain.exception.AlreadyBookedTicketException;
import com.heeverse.ticket_order.domain.exception.LockOccupancyFailureException;
import com.heeverse.ticket_order.domain.exception.TicketAggregationFailException;
import com.heeverse.ticket_order.domain.exception.TicketingFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author gutenlee
 * @since 2023/10/06
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


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
    public ResponseEntity<?> badRequestException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorMessage.convert(HttpStatus.BAD_REQUEST)));
    }


    @ExceptionHandler({
            AuthenticationServiceException.class
    })
    public ResponseEntity<?> authException() {
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
                .body(ErrorMessage.convert(HttpStatus.INTERNAL_SERVER_ERROR));
    }


}
