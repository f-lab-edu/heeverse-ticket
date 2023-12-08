package com.heeverse.member.dto;

import lombok.Getter;

@Getter
public class AuthenticatedMember {
    private final long seq;
    private final String memberId;

    public AuthenticatedMember(long seq, String memberId) {
        this.seq = seq;
        this.memberId = memberId;
    }
}
