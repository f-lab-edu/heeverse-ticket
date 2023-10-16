package com.heeverse;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * @author gutenlee
 * @since 2023/08/26
 */
public class ControllerTestHelper {


    public static MockMvc getSecurityMockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    public static MockMvc getNotSecuredMockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context).build();
    }

    public static MockMvc getRestDocsMockMvc(
            WebApplicationContext wac,
            RestDocumentationContextProvider restDocumentation
    ) {
        return MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    public static class Endpoint {
        public static class Member {
            public final static String 로그인 ="/login";
            public final static String 회원가입 = "/member";
        }

        public static class CONCERT {
            public final static String 콘서트_등록 = "/concert";
        }

        public static class TICKET {
            public final static String 티켓_예매 = "/ticket-order";
            public final static String 잔여_티켓_집계 = "/ticket-order/remains";
            public final static String 티켓_예매_집계 = "/ticket-order/log";
        }
    }
}
