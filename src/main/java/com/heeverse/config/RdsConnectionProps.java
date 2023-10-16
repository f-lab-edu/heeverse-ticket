package com.heeverse.config;

import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import java.lang.management.ManagementFactory;

import static com.heeverse.common.Constants.MYSQL_SECRETES;
import static com.heeverse.common.Constants.VAULT_PATH;


/**
 * @author gutenlee
 * @since 2023/08/21
 */
@Primary
@Configuration
@Profile(value = {"dev-test", "prod"})
public class RdsConnectionProps extends DataSourceProperties {

    private final VaultOperationService vaultOperationService;

    @Autowired
    public RdsConnectionProps(VaultOperationService vaultOperationService) {
        this.vaultOperationService = vaultOperationService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        var dbProps = vaultOperationService.getProps(VAULT_PATH, MYSQL_SECRETES, DBProps.class);

        this.setUsername(dbProps.username());
        this.setUrl(dbProps.url());
        this.setPassword(dbProps.password());

        super.afterPropertiesSet();
    }

    private record DBProps(
            String url,
            String username,
            String password
    ) {
        DBProps {
            String name = this.getClass().getSimpleName();
            Assert.notNull(url, name + "'s [url] must not null!");
            Assert.notNull(username, name + "'s [username] must not null!");
            Assert.notNull(password, name + "'s [password] must not null!");
        }
    }

    @Bean
    public HikariPoolMXBean poolProxy() throws MalformedObjectNameException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.zaxxer.hikari:type=Pool (hikari)");
        return JMX.newMBeanProxy(mBeanServer, objectName, HikariPoolMXBean.class);
    }

}
