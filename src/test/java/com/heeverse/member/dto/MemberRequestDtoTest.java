package com.heeverse.member.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRequestDtoTest {

    @DisplayName("아이디_정규표현식_검사")
    @Test
    void memberIdRegexTrueTest() {
        //given
        String memberId = "gutenlee1128";
        String regex = "^[a-z]+[a-z0-9]{5,15}$";

        //when
        boolean result = regexMatching(memberId, regex);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("아이디_정규표현식_예외_검사")
    @Test
    void memberIdRegexFalseTest() {
        //given
        String memberId = "gu&ten_lee1128";
        String regex = "^[a-z]+[a-z0-9]{5,15}$";

        //when
        boolean result = regexMatching(memberId, regex);

        //then
        Assertions.assertFalse(result);
    }

    @DisplayName("비밀번호_정규표현식_검사")
    @Test
    void memberPwdRegexTrueTest() {
        //given
        String memberPwd = "sjk!fsl@f99393A";
        String regex = "^(?=.*[a-zA-Z])(?=.*[@\\!\\?$%^*+=-])(?=.*[0-9]).{8,15}$";

        //when
        boolean result = regexMatching(memberPwd, regex);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("비밀번호_정규표현식_예외_검사")
    @Test
    void memberPwdRegexFalseTest() {
        //given
        String memberPwd = "sjk!fsl@f9<>9393A";
        String regex = "^(?=.*[a-zA-Z])(?=.*[@\\!\\?$%^*+=-])(?=.*[0-9]).{8,15}$";

        //when
        boolean result = regexMatching(memberPwd, regex);

        //then
        Assertions.assertFalse(result);
    }

    private boolean regexMatching(String testStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testStr);

        return matcher.find();

    }
}