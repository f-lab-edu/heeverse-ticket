package com.heeverse.member.domain;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.dto.MemberRequestDto;

/**
 * @author jeongheekim
 * @date 2023/09/23
 */
public class MemberTestHelper {
    public static final String MEMBER_ID = "guestzzang";
    public static final String NAME = "armyBTS";
    public static final String PASSWORD = "Helloworld!123";
    public static final String EMAIL = "guestzzang@gmail.com";

    private MemberTestHelper() {
    }

    public static Member getMockMember() {
        return new Member(MEMBER_ID, NAME, PASSWORD, EMAIL);
    }

}
