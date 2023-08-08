package com.heeverse.member.domain.entity;

import com.heeverse.common.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.apache.ibatis.annotations.Param;

@Getter
public class Member extends BaseEntity {

    private Long memberId;
    private String id;
    private String password;
    private String userName;
    private String email;

    @Builder
    public Member(
            @Param ("id") @NotNull String id,
            @Param("password")@NotNull String password,
            @Param("userName") String userName,
            @Param("email") String email) {
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }
}
