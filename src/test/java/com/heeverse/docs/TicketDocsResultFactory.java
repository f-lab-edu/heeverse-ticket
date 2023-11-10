package com.heeverse.docs;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.ResultHandler;

import static com.heeverse.docs.ApiDocumentUtils.getDocumentRequest;
import static com.heeverse.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author gutenlee
 * @since 2023/10/10
 */
public class TicketDocsResultFactory {

    public static ResultHandler tickerOrderSuccessDocs() {
        return document("ticket-order",
                getDocumentRequest(),
                getDocumentResponse(),
                getTicketOrderRequestSnippet(),
                responseFields(
                        fieldWithPath("[]concertName").type(JsonFieldType.STRING).description("티켓 등급"),
                        fieldWithPath("[]concertDate").type(JsonFieldType.STRING).description("공연 일시"),
                        fieldWithPath("[]ticketSerialNumber").type(JsonFieldType.STRING).description("가수 한글명"),
                        fieldWithPath("[]gradeName").type(JsonFieldType.STRING).description("가수 영문명"),
                        fieldWithPath("[]bookingDate").type(JsonFieldType.STRING).description("공연장"),
                        fieldWithPath("[]bookingStatus").type(JsonFieldType.STRING).description("티켓 개수")
                )
        );
    }

    public static ResultHandler ticketOrderErrorDocs() {
        return document("error/ticket-order",
                getDocumentRequest(),
                getDocumentResponse(),
                getTicketOrderRequestSnippet(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp")
                )
        );
    }


    public static ResultHandler tickerRemainsResponseDocs() {
        return document("ticket-order/remains",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("concertSeq").type(JsonFieldType.NUMBER).description("공연 시퀀스")
                ),
                responseFields(
                        fieldWithPath("[]concertSeq").type(JsonFieldType.NUMBER).description("콘서트 시퀀스"),
                        fieldWithPath("[]gradeTicket").type(JsonFieldType.STRING).description("티켓 등급"),
                        fieldWithPath("[]remain").type(JsonFieldType.NUMBER).description("잔여 티켓 수")
                )
        );
    }

    public static ResultHandler ticketOrderLogDocs() {
        return document("ticket-order/log",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("concertSeq").type(JsonFieldType.NUMBER).description("공연 시퀀스"),
                        fieldWithPath("query").type(JsonFieldType.BOOLEAN).description("쿼리로 처리 여부"),
                        fieldWithPath("normalization").type(JsonFieldType.BOOLEAN).description("집계 쿼리 사용 시 정규화된 테이블에서 조회 여부"),
                        fieldWithPath("strategyType").type(JsonFieldType.STRING).description("전략 타입 종류"),
                        fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("페이징 사이즈")
                ),
                responseFields(
                        fieldWithPath("[]concertSeq").type(JsonFieldType.NULL).description("공연 시퀀스"),
                        fieldWithPath("[]gradeName").type(JsonFieldType.NULL).description("티켓 등급"),
                        fieldWithPath("[]totalTickets").type(JsonFieldType.NULL).description("전체 티켓 수"),
                        fieldWithPath("[]orderTry").type(JsonFieldType.NULL).description("예매 시도 수"),
                        fieldWithPath("[]message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("[]createdAt").type(JsonFieldType.STRING).description("작업 시작일시")
                )
        );
    }


    public static ResultHandler tickerRemainsErrorDocs() {
        return document("error/ticket-order/remains",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("concertSeq").type(JsonFieldType.NUMBER).description("공연 시퀀스")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp")
                )
        );
    }

    private static RequestFieldsSnippet getTicketOrderRequestSnippet() {
        return requestFields(
                fieldWithPath("ticketSetList").type(JsonFieldType.ARRAY).description("티켓 시퀀스")
                        .attributes(key("티켓 예매").value("티켓 시퀀스"))
        );
    }
}
