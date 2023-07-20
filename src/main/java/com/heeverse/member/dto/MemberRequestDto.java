package com.heeverse.member.dto;

import jakarta.validation.constraints.Pattern;

public class MemberRequestDto {

    static final String MEMBER_ID_REGEX = "^[a-z]+[a-z0-9]{5,15}$";
    static final String PWD_REGEX = "^(?=.*[a-zA-Z])(?=.*[@\\!\\?$%^*+=-])(?=.*[0-9]).{8,15}$";
    static final String USER_NAME_REGEX = "^[a-zA-Zㄱ-힣 ]{1,20}$";
    static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Pattern(regexp = MEMBER_ID_REGEX, message = "id형식에 맞지 않습니다.")
    private String id;
    @Pattern(regexp = PWD_REGEX, message = "비밀번호 형식에 맞지 않습니다.")
    private String password;
    @Pattern(regexp = USER_NAME_REGEX, message = "사용자 이름 형식에 맞지 않습니다.")
    private String userName;
    @Pattern(regexp = EMAIL_REGEX, message = "이메일 형식에 맞지 않습니다.")
    private String email;

}
