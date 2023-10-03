package com.heeverse.member.domain;

import com.github.javafaker.Faker;
import com.heeverse.member.domain.entity.Member;

import java.util.Locale;

/**
 * @author jeongheekim
 * @date 2023/09/23
 */
public class MemberTestHelper {
    public static final String MEMBER_ID = "guestzzang";

    public static final String PASSWORD = "Helloworld!123";

    private MemberTestHelper() {
    }

    public static Member getMockMember() {
        Faker faker = new Faker(Locale.US);
        return new Member(MEMBER_ID + faker.number().randomDigit(), PASSWORD, faker.name().name(), faker.internet().emailAddress());
    }

}
