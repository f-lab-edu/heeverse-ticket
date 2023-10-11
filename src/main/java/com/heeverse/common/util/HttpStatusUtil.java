package com.heeverse.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author gutenlee
 * @since 2023/10/10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpStatusUtil {

    public static boolean isError(HttpStatus status) {
        return !status.is2xxSuccessful();
    }

    public static boolean isSuccess(HttpStatus status) {
        return !isError(status);
    }

    public static boolean isNull(HttpStatus status) {
        return status == null;
    }
}
