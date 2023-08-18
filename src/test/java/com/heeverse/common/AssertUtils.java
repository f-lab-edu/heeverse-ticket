package com.heeverse.common;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * @author gutenlee
 * @since 2023/08/17
 */
public class AssertUtils {

    /**
     * 생성자, builder 에서 NPE 테스트
     * @param runMethod
     */
    public static void assertThrowNPE(RunMethod runMethod) {
        assertThrowsExactly(NullPointerException.class, runMethod::execute);
    }


    public interface RunMethod {
        void execute();
    }
}
