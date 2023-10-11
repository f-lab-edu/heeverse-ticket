package com.heeverse.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author gutenlee
 * @since 2023/08/10
 */
@UtilityClass
public class StringUtils {

    public static String leftPad(final String str, final int size, final char padChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(padChar);
        }

        return sb.substring(str.length()) + str;
    }

}
