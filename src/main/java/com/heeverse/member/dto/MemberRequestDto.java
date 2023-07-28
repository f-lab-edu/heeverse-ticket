package com.heeverse.member.dto;

import com.heeverse.common.util.RegexUtils;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequestDto {

    @Pattern(regexp = RegexUtils.MEMBER_ID_REGEX, message = "id형식에 맞지 않습니다.")
    private String id;

    @Pattern(regexp = RegexUtils.PWD_REGEX, message = "비밀번호 형식에 맞지 않습니다.")
    private String password;

    @Pattern(regexp = RegexUtils.USER_NAME_REGEX, message = "사용자 이름 형식에 맞지 않습니다.")
    private String userName;

    @Pattern(regexp = RegexUtils.EMAIL_REGEX, message = "이메일 형식에 맞지 않습니다.")
    private String email;

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
