package com.heeverse.docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

import static com.heeverse.docs.ApiDocumentUtils.getDocumentRequest;
import static com.heeverse.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

/**
 * @author gutenlee
 * @since 2023/10/10
 */
public class MemberDocsResultFactory {

    public static RestDocumentationResultHandler memberSuccessDocs() {
        return document("member",
                getDocumentRequest(),
                getDocumentResponse(),
                getCommonRequestSnippet()
        );
    }

    public static RestDocumentationResultHandler memberErrorDocs() {
        return document("member",
                getDocumentRequest(),
                getDocumentResponse(),
                getCommonRequestSnippet(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp")
                )
        );
    }

    private static RequestFieldsSnippet getCommonRequestSnippet() {
        return requestFields(
                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
        );
    }
}
