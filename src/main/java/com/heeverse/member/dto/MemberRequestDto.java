package com.heeverse.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MemberRequestDto {

    @NotNull
    @Pattern(regexp = "^[a-z]+[a-z0-9]{5,15}$", message = "id형식에 맞지 않습니다.")
    private String id;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[@\\!\\?$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호 형식에 맞지 않습니다.")
    private String password;
    private String userName;
    private String email;

}
