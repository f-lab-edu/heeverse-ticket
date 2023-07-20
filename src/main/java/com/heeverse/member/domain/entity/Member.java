package com.heeverse.member.domain.entity;

import com.heeverse.common.BaseEntity;

public class Member extends BaseEntity {

    private Integer memberId;
    private String id;
    private String password;
    private String userName;
    private String email;

    public Member(String id, String password, String userName, String email) {
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
