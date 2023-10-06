package com.heeverse.common.factory;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.domain.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jeongheekim
 * @date 2023/09/28
 */
@Component
public class MemberFactory {

    @Autowired
    private MemberMapper memberMapper;

    public Long createMember(Member member) {
        memberMapper.insertMember(member);
        return member.getSeq();
    }

}
