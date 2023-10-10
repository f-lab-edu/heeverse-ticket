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
        return document("ticket-order",
                getDocumentRequest(),
                getDocumentResponse(),
                getTicketOrderRequestSnippet(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp")
                )
        );
    }

    public static ResultHandler tickerRemainsErrorDocs() {
        return document("ticket-order/remains",
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
