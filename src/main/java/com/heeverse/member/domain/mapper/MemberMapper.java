package com.heeverse.member.domain.mapper;

import com.heeverse.member.domain.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jeongheekim
 * @date 2023/07/22
 */
@Mapper
public interface MemberMapper {

    Member findById(@Param("id") String id);

    Long insertMember(Member member);
}
