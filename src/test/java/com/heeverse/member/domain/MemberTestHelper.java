package com.heeverse.member.domain;

import com.github.javafaker.Faker;
import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.dto.MemberRequestDto;

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
        Faker faker = fakerLocale(Locale.US);
        String fakeName = fakerLocale(Locale.KOREA).name().name();
        return new Member(MEMBER_ID + fakeName, PASSWORD, fakeName, faker.internet().emailAddress());
    }

    public static MemberRequestDto mockingMemberRequestDto() {
        Faker kor = fakerLocale(Locale.KOREA);
        String fakeName = kor.name().name();

        Faker us = fakerLocale(Locale.US);
        return new MemberRequestDto(MEMBER_ID, PASSWORD, fakeName, us.internet().emailAddress());

    }

    public static Faker fakerLocale(Locale locale) {
        return new Faker(locale);
    }

}
