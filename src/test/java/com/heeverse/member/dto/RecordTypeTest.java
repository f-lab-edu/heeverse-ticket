package com.heeverse.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordTypeTest {

    @Test
    @DisplayName("record 타입 인자에 @NonNull 어노테이션 선언시 null 값 들어오면 NPE 발생한다")
    void nonNullTest() throws Exception {

        assertThrows(NullPointerException.class, () -> new LoginRequestDto(null, "234"));
    }


}