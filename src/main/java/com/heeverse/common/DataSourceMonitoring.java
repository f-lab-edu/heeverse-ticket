package com.heeverse.common;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gutenlee
 * @since 2023/10/16
 */
@Component
@Slf4j
public class DataSourceMonitoring implements DataSourceMonitoringMBean {

    private final HikariDataSource dataSource;

    public DataSourceMonitoring(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getnumActive() {
        return dataSource.getHikariPoolMXBean().getActiveConnections();
    }

    @Override
    public int getnumIdle() {
        return dataSource.getHikariPoolMXBean().getIdleConnections();
    }

    @Override
    public int getmaxTotal() {
        return dataSource.getHikariConfigMXBean().getMaximumPoolSize();
    }
}
