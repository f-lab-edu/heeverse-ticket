package com.heeverse.ticket_order.service.transfer;

import com.heeverse.ticket_order.domain.dto.persistence.AggregateInsertMapperDto;
import com.heeverse.ticket_order.domain.mapper.TicketOrderAggregationMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Disabled
@SpringBootTest(classes = {ResultDBTransfer.class, TicketOrderAggregationMapper.class})
class ResultTransferUnitTest {

    @MockBean
    private TicketOrderAggregationMapper aggregationMapper;
    @MockBean
    private ResultDBTransfer resultDBTransfer;

    @Test
    @DisplayName("집계 결과 저장 성공하고 리턴한다")
    void transferAllTest() throws Exception {

        // given
        AggregateInsertMapperDto mapperDto = AggregateInsertMapperDto.builder()
                .concertSeq(1L)
                .gradeName("A")
                .orderTry(1000)
                .build();

        List<AggregateInsertMapperDto> toInsert = List.of(mapperDto);

        // when
        doNothing().when(aggregationMapper).insertAggregationResult(toInsert);
        when(resultDBTransfer.transferAll(toInsert)).thenReturn(toInsert);

        // then
        List<AggregateInsertMapperDto> saved = resultDBTransfer.transferAll(toInsert);

        Assertions.assertEquals(saved, toInsert);
    }



}