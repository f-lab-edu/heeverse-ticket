package com.heeverse.member.domain.entity;

import com.heeverse.common.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member extends BaseEntity {

    private Long memberId;
    private String id;
    private String password;
    private String userName;
    private String email;

    @Builder
    public Member(@NotNull String id, @NotNull String password, String userName, String email) {
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
