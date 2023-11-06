package com.heeverse.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author gutenlee
 * @since 2023/10/29
 */
@UtilityClass
public class PrimitiveUtils {

    public static int toIntSafely(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow 발생합니다 : " + value);
        }
        return (int) value;
    }
}
