package com.heeverse.common;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
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

    @Bean
    public HikariPoolMXBean poolProxy() throws MalformedObjectNameException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.heeverse.hikari:type=Pool (hikari)");
        return JMX.newMBeanProxy(mBeanServer, objectName, HikariPoolMXBean.class);
    }
}
