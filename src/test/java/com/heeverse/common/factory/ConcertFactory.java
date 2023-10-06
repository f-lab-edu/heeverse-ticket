package com.heeverse.common.factory;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author jeongheekim
 * @date 2023/09/27
 */
@Component
public class ConcertFactory {

    @Autowired
    private ConcertMapper concertMapper;

    public void registerConcert(Concert concert) {
        concertMapper.insertConcert(concert);
    }

    public Long selectLatestConcertSeq() {
        return concertMapper.selectLatestConcertSeq();
    }
}
