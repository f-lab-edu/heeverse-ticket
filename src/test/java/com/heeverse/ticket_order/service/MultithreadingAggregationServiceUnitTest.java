package com.heeverse.ticket_order.service;

import com.heeverse.ticket_order.domain.dto.enums.StrategyType;
import com.heeverse.ticket_order.service.reader.MultiAggregationReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.any;


class MultithreadingAggregationServiceUnitTest {

    ApplicationContext ac = new
            AnnotationConfigApplicationContext(AppConfig.class);
    static class AppConfig {
        @Bean
        public MultiAggregationReader multiAggregationReader() {
            return new MultiAggregationReader(any(), any(), any());
        }
    }


    @Test
    @DisplayName("MultiAggregationReader 클래스 타입 리턴 테스트")
    void getReaderBeanTest() throws Exception {

        //given
        StrategyType type = StrategyType.MULTI_THREAD;

        Assertions.assertInstanceOf(MultiAggregationReader.class, ac.getBean(StrategyType.getReaderClazz(type)));
    }



}