package com.heeverse.concert.domain.mapper;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.dto.persistence.ConcertResponseMapperDto;
import com.heeverse.concert.dto.persistence.ConcertRequestMapperDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Mapper
public interface ConcertMapper {
    Long insertConcert(Concert concert);
    List<ConcertResponseMapperDto> selectConcertList(ConcertRequestMapperDto dto);
}
