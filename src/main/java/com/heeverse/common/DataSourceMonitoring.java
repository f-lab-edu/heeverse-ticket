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

    public DataSourceMonitoring(@Qualifier("lockDataSource") HikariDataSource lockDataSource) {
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
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.heeverse:type=HikariDataSource,name=HikariPool-1,context=/");

        DataSourceMonitoring dataSourceMonitoring = new DataSourceMonitoring(lockDataSource);

        mBeanServer.registerMBean(dataSourceMonitoring, objectName);
    }
}
