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
    public static final Faker usFaker = new Faker(Locale.US);
    public static final Faker korFaker = new Faker(Locale.KOREA);

    private MemberTestHelper() {
    }

    public static Member getMockMember() {
        String fakeName = usFaker.name().name();
        String koreanName = korFaker.name().name();
        return new Member(MEMBER_ID + fakeName, PASSWORD, koreanName, usFaker.internet().emailAddress());
    }

    public static MemberRequestDto getMockMemberRequestDto() {
        String fakeName = korFaker.name().name();
        return new MemberRequestDto(MEMBER_ID, PASSWORD, fakeName, usFaker.internet().emailAddress());
    }
}
