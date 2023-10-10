package com.heeverse.docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

import static com.heeverse.docs.ApiDocumentUtils.getDocumentRequest;
import static com.heeverse.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author gutenlee
 * @since 2023/10/10
 */
public class ConcertDocsResultFactory {

    public static RestDocumentationResultHandler concertSuccessDocs() {
        return document("concert",
                getDocumentRequest(),
                getDocumentResponse(),
                getCommonRequestSnippet(),
                responseFields(
                        fieldWithPath("[]concertName").type(JsonFieldType.STRING).description("티켓 등급"),
                        fieldWithPath("[]concertDate").type(JsonFieldType.STRING).description("공연 일시"),
                        fieldWithPath("[]artistNameKor").type(JsonFieldType.STRING).description("가수 한글명"),
                        fieldWithPath("[]artistNameEng").type(JsonFieldType.STRING).description("가수 영문명"),
                        fieldWithPath("[]venueName").type(JsonFieldType.STRING).description("공연장"),
                        fieldWithPath("[]ticketCount").type(JsonFieldType.NUMBER).description("티켓 개수")
                )
        );
    }

    public static RestDocumentationResultHandler errorDocs() {
        return document("concert",
                getDocumentRequest(),
                getDocumentResponse(),
                getCommonRequestSnippet(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp")
                )
        );
    }

    private static RequestFieldsSnippet getCommonRequestSnippet() {
        return requestFields(
                fieldWithPath("[]concertName").type(JsonFieldType.STRING).description("공연명"),
                fieldWithPath("[]concertDate").type(JsonFieldType.STRING).description("공연 날짜"),
                fieldWithPath("[]ticketOpenTime").type(JsonFieldType.STRING).description("티켓 예매 오픈일"),
                fieldWithPath("[]ticketEndTime").type(JsonFieldType.STRING).description("티켓 예매 종료일"),
                fieldWithPath("[]artistSeq").type(JsonFieldType.NUMBER).description("아티스트 seq"),
                fieldWithPath("[]venueSeq").type(JsonFieldType.NUMBER).description("공연장 seq"),
                fieldWithPath("[]ticketGradeDtoList").type(JsonFieldType.ARRAY).description("티켓 등급 리스트"),
                fieldWithPath("[]ticketGradeDtoList[].gradeName").type(JsonFieldType.STRING).description("티켓 등급"),
                fieldWithPath("[]ticketGradeDtoList[].seatCount").type(JsonFieldType.NUMBER).description("좌석 수")
        );
    }


}
