package com.heeverse.common;


import lombok.experimental.UtilityClass;

import java.util.concurrent.*;

/**
 * @author gutenlee
 * @since 2023/10/17
 */
@UtilityClass
public class ExecutorUtils {

    public static ExecutorService createFixedThreadPool(final int threads) {
        return Executors.newFixedThreadPool(threads);
    }


}
