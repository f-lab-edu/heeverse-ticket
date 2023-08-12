package com.heeverse.concert.domain.mapper;

import com.heeverse.concert.domain.entity.Concert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Mapper
public interface ConcertMapper {

    Long insertConcert(Concert concert);
}
