package com.heeverse.common;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author jeongheekim
 * @date 2023/09/09
 */
@Mapper
public interface LockMapper {
    void getLock(NamedLockInfo namedLockInfo);
    void releaseLock(NamedLockInfo namedLockInfo);

}
