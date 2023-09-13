package com.heeverse.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jeongheekim
 * @date 2023/09/09
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LockTemplate {

    private final LockMapper lockMapper;

    public void getLock(String userLockName, int timeoutSeconds) {
        log.info("start get Lock :{} , timeout : {}", userLockName, timeoutSeconds);
        lockMapper.getLock(new NamedLockInfo(userLockName, timeoutSeconds));
        log.info("success get Lock :{} , timeout : {}", userLockName, timeoutSeconds);
    }

    public void releaseLock(String userLockName) {
        log.info("start release Lock :{}", userLockName);
        lockMapper.releaseLock(new NamedLockInfo(userLockName, null));
        log.info("success release Lock :{} ", userLockName);
    }

}
