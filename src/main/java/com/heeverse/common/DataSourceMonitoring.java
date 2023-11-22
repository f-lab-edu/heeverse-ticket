package com.heeverse.common;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author gutenlee
 * @since 2023/10/16
 */
@Component
@Slf4j
public class DataSourceMonitoring implements DataSourceMonitoringMBean {

    private final HikariDataSource lockDataSource;

    public DataSourceMonitoring(HikariDataSource lockDataSource) {
        this.lockDataSource = lockDataSource;
    }

    @Override
    public int getnumActive() {
        return lockDataSource.getHikariPoolMXBean().getActiveConnections();
    }

    @Override
    public int getnumIdle() {
        return lockDataSource.getHikariPoolMXBean().getIdleConnections();
    }

    @Override
    public int getmaxTotal() {
        return lockDataSource.getHikariConfigMXBean().getMaximumPoolSize();
    }

    @PostConstruct
    public void init() throws Exception {
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, new ObjectName("com.xxx:type=DataSource,name=HikariPool-1"));
        } catch (Exception e) {
            log.error("MBean Register Error", e);
        }
    }
}
