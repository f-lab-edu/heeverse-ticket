package com.heeverse.common;

/**
 * @author jeongheekim
 * @date 2023/09/09
 */
public class NamedLockInfo {
    private final String lockName;
    private final Integer timeoutSeconds;

    public NamedLockInfo(String lockName, Integer timeoutSeconds) {
        this.lockName = lockName;
        this.timeoutSeconds = timeoutSeconds;
    }

}
