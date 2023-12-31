package com.heeverse.concert.domain.mapper;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.dto.persistence.ConcertRequestMapperDto;
import com.heeverse.concert.dto.persistence.ConcertResponseMapperDto;
import com.heeverse.concert.dto.persistence.RegisteredConcertMapperDto;
import com.heeverse.config.LockScanMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@Mapper
@LockScanMapper
public interface ConcertMapper {
    void insertConcert(Concert concert);
    List<ConcertResponseMapperDto> selectConcertList(ConcertRequestMapperDto dto);
    Long selectLatestConcertSeq();
    List<RegisteredConcertMapperDto> selectRegisteredConcertList(@Param("concertSeqList") List<Long> concertSeqList);
}
