package com.heeverse.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/09/15
 */
class StringUtilsTest {
    @Test
    @DisplayName("Long 타입 List를 통해 delimiter로 join된 string값을 만든다.")
    void createSeqListStrTest() {
        List<Long> seqList = Arrays.asList(1L, 2L, 3L);
        String delimiter = "_";
        Assertions.assertEquals(StringUtils.createSeqListStr(seqList, delimiter), "1_2_3");
    }

    @Test
    @DisplayName("String 타입 List를 통해 delimiter로 join된 string값을 만든다.")
    void createSeqListStrTest2() {
        List<String> seqList = Arrays.asList("hello", "world", "happy");
        Assertions.assertEquals(StringUtils.createSeqListStr(seqList, null), "hello,world,happy");
    }

}