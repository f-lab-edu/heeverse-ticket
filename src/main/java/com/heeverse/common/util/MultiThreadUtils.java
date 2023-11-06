package com.heeverse.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author gutenlee
 * @since 2023/10/29
 */
@UtilityClass
public class MultiThreadUtils {

    public int calculateTaskSizePerCore(int origin) {
        if (origin < 0) {
            throw new IllegalArgumentException("작업 사이즈가 부적절합니다 : " + origin);
        }
        return origin / Runtime.getRuntime().availableProcessors();
    }

    public int availableCores() {
        return Runtime.getRuntime().availableProcessors();
    }
}
