package com.heeverse.common.util;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gutenlee
 * @since 2023/08/10
 */
public class StringUtils {

    public static String leftPad(final String str, final int size, final char padChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(padChar);
        }

        return sb.substring(str.length()) + str;
    }

    public static <T> String createSeqListStr(final @NotNull List<T> seqList, String delimiter) {
        if (Objects.isNull(delimiter)) {
            delimiter = ",";
        }
        return seqList
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

}
