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
        return status.is4xxClientError() || status.is5xxServerError();
    }

    public static boolean isSuccess(HttpStatus status) {
        return status.is2xxSuccessful()
                || status.is1xxInformational()
                    || status.is3xxRedirection();
    }

    public static boolean isEmpty(HttpStatus status) {
        return status == null;
    }
}
