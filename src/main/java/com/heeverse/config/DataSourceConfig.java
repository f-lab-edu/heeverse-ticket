package com.heeverse.config;

import com.heeverse.common.DataSourceMonitoring;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author gutenlee
 * @since 2023/10/16
 */
@Configuration
@MapperScan(basePackages = "com.heeverse")
@AllArgsConstructor
public class DataSourceConfig {

    private HikariDataSource dataSource;

    @PostConstruct
    public void init() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.heeverse:type=DataSource,name=hikari-pool,context=/");

        DataSourceMonitoring dataSourceMonitoring = new DataSourceMonitoring(dataSource);

        mBeanServer.registerMBean(dataSourceMonitoring, objectName);
    }

}
