package com.heeverse.common;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author gutenlee
 * @since 2023/10/16
 */
@Component
@Slf4j
public class DataSourceMonitoring implements DataSourceMonitoringMBean {

    @Autowired
    private HikariDataSource dataSource;

    @Override
    public int getnumActive() {
        // PoolMXBean 이 초기화 되지 않으면 null 에러 발생 방지
        return dataSource.getHikariPoolMXBean() != null ? dataSource.getHikariPoolMXBean().getActiveConnections() : 0;
    }

    @Override
    public int getnumIdle() {
        return dataSource.getHikariPoolMXBean() != null ? dataSource.getHikariPoolMXBean().getIdleConnections() : 0;
    }

    @Override
    public int getmaxTotal() {
        return dataSource.getHikariConfigMXBean().getMaximumPoolSize();
    }

    @PostConstruct
    public void init() {
        try {
            // type 이 DataSource 이어야 한다. name 은 아무거나 원하는 것 셋팅
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, new ObjectName("com.xxx:type=DataSource,name=rds1"));
        } catch (Exception e) {
            log.error("MBean Register Error", e);
        }
    }
}
