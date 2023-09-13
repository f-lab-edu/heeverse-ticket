package com.heeverse.ticket_order.controller;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.ticket_order.domain.dto.TicketOrderRequestDto;
import com.heeverse.ticket_order.domain.dto.TicketOrderResponseDto;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import com.heeverse.ticket_order.service.TicketOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/30
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket-order")
public class TicketOrderController {

    private final TicketOrderFacade ticketOrderFacade;
    private final TicketOrderService ticketOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<TicketOrderResponseDto>> orderTicket(@RequestBody @Valid TicketOrderRequestDto dto, Authentication authentication) throws Exception{
        Member member = (Member) authentication.getPrincipal();
        Long ticketOrderSeq = ticketOrderFacade.startTicketOrderJob(dto, member.getSeq());
        return ResponseEntity.ok(ticketOrderService.getOrderTicket(ticketOrderSeq));

    }
}
