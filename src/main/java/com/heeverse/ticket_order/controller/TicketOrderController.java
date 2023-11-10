package com.heeverse.ticket_order.controller;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.ticket_order.domain.dto.*;
import com.heeverse.ticket_order.domain.dto.persistence.AggregateSelectMapperDto;
import com.heeverse.ticket_order.service.MultithreadingAggregationService;
import com.heeverse.ticket_order.service.QueryAggregationService;
import com.heeverse.ticket_order.service.TicketOrderFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.heeverse.common.IndexController.INDEX_URI;

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
    private final QueryAggregationService queryAggregationService;
    private final MultithreadingAggregationService multithreadingAggregationService;

    @PostMapping
    public ResponseEntity<List<TicketOrderResponseDto>> orderTicket(
            @RequestBody @Valid TicketOrderRequestDto dto,
            @AuthenticationPrincipal Member member) throws Exception {

        return ResponseEntity
                .created(URI.create(INDEX_URI))
                .body(ticketOrderFacade.startTicketOrderJob(dto, member.getSeq()));
    }


    @GetMapping("/remains")
    public ResponseEntity<List<TicketRemainsResponseDto>> aggregateTicketRemains(
            @RequestBody TicketRemainsDto ticketRemainsDto) {
        return ResponseEntity.ok(ticketOrderFacade.getTicketRemains(ticketRemainsDto));
    }


    @GetMapping("/log")
    public ResponseEntity<List<AggregateDto.Response>> aggregate(
            @RequestBody AggregateDto.Request request
    ) {
        log.info("Request : 전략 {} / pageSize {}", request.getStrategyType(), request.getPageSize());

        if (request.isQuery()) {
            return ResponseEntity.ok(queryAggregationService.aggregate(AggregateSelectMapperDto.QueryRequest.of(request)));
        }

        multithreadingAggregationService.aggregate(AggregateSelectMapperDto.Request.of(request));
        return ResponseEntity.ok(List.of(new AggregateDto.Response()));
    }
}
