package com.heeverse.common;

import com.heeverse.common.factory.ConcertFactory;
import com.heeverse.common.factory.MemberFactory;
import com.heeverse.common.factory.TicketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/09/28
 */
@Transactional
@ActiveProfiles("dev")
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected MemberFactory memberFactory;
}
