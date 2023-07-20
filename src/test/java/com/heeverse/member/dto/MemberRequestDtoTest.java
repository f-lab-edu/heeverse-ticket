package com.heeverse.member.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberRequestDtoTest {

    @DisplayName("아이디_정규표현식_검사")
    @Test
    void memberIdRegexTrueTest() {
        //given
        String memberId = "gutenlee1128";

        //when
        boolean result = regexMatching(memberId, MemberRequestDto.MEMBER_ID_REGEX);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("아이디_정규표현식_예외_검사")
    @Test
    void memberIdRegexFalseTest() {
        //given
        String memberId = "gu&ten_lee1128";

        //when
        boolean result = regexMatching(memberId, MemberRequestDto.MEMBER_ID_REGEX);

        //then
        Assertions.assertFalse(result);
    }

    @DisplayName("비밀번호_정규표현식_검사")
    @Test
    void memberPwdRegexTrueTest() {
        //given
        String memberPwd = "sjk!fsl@f99393A";

        //when
        boolean result = regexMatching(memberPwd, MemberRequestDto.PWD_REGEX);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("비밀번호_정규표현식_예외_검사")
    @Test
    void memberPwdRegexFalseTest() {
        //given
        String memberPwd = "sjk!fsl@f9<>9393A";

        //when
        boolean result = regexMatching(memberPwd, MemberRequestDto.PWD_REGEX);

        //then
        Assertions.assertFalse(result);
    }


    @DisplayName("사용자_이름_정규표현식_검사")
    @ParameterizedTest
    @ValueSource(strings = {"홍길동", "Romeo", "데이비드 Kim"})
    void userNameRegexTrueTest(String userName) {
        //when
        boolean result = regexMatching(userName, MemberRequestDto.USER_NAME_REGEX);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("사용자_이름_정규표현식_예외_검사")
    @ParameterizedTest
    @ValueSource(strings = {"abcdefghijabcdefghija", "Romeo!"})
    void userNameRegexFalseTest(String userName) {
        //when
        boolean result = regexMatching(userName, MemberRequestDto.USER_NAME_REGEX);

        //then
        Assertions.assertFalse(result);
    }

    @DisplayName("이메일_정규표현식_검사")
    @Test
    void emailRegexTrueTest() {
        //given
        String email = "hello342@gmail.com";
        //when
        boolean result = regexMatching(email, MemberRequestDto.EMAIL_REGEX);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("이메일_정규표현식_예외_검사")
    @Test
    void emailRegexFalseTest() {
        //given
        String email = "hello342#gmail.com";
        //when
        boolean result = regexMatching(email, MemberRequestDto.EMAIL_REGEX);

        //then
        Assertions.assertFalse(result);
    }

    private boolean regexMatching(String testStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testStr);

        return matcher.find();
    }
}