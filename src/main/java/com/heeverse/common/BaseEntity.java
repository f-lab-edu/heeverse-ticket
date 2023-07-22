package com.heeverse.common;

import java.time.LocalDateTime;

/**
 * @author gutenlee
 * @since 2023/07/19
 */
public abstract class BaseEntity {
    private final LocalDateTime createDateTime;

    protected BaseEntity() {
        this.createDateTime = LocalDateTime.now();
    }
}
