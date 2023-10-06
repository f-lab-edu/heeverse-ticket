package com.heeverse.common;

import com.heeverse.common.factory.MemberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/09/28
 */
@Transactional
@ActiveProfiles("dev-test")
@SpringBootTest
public abstract class IntegrationTestService {

    @Autowired
    protected MemberFactory memberFactory;
}
