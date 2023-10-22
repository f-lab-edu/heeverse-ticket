package com.heeverse.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author gutenlee
 * @since 2023/10/22
 */

@Component
public class ExecutorsComponent {

    public static int availableCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Bean
    public ExecutorService fixedPool() {
        return Executors.newFixedThreadPool(availableCores() * 2);
    }
    @Bean
    public ExecutorService singlePool() {
        return Executors.newSingleThreadExecutor();
    }
}
