package com.heeverse.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author jeongheekim
 * @date 10/28/23
 */
@Configuration
@MapperScan(value = "com.heeverse.ticket.domain.mapper", sqlSessionFactoryRef = "lockSqlSessionFactory")
public class LockSqlSessionFactoryConfiguration {

    @Bean(name = "lockSqlSessionFactory")
    public SqlSessionFactory lockSqlSessionFactory(@Qualifier("lockDataSource") DataSource lockDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(lockDataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/lock-mapper/**.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "lockSqlSessionTemplate")
    public SqlSessionTemplate lockSqlSessionTemplate(@Qualifier("lockSqlSessionFactory") SqlSessionFactory lockSqlSessionFactory) {
        return new SqlSessionTemplate(lockSqlSessionFactory);
    }

}
