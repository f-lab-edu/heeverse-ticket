package com.heeverse.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import static com.heeverse.common.Constants.MYSQL_SECRETES;
import static com.heeverse.common.Constants.VAULT_PATH;


/**
 * @author gutenlee
 * @since 2023/08/21
 */
@Primary
@Configuration
@Profile(value = {"dev-test", "prod", "dev"})
public class RdsConnectionProps {

    private final VaultOperationService vaultOperationService;

    @Autowired
    public RdsConnectionProps(VaultOperationService vaultOperationService) {
        this.vaultOperationService = vaultOperationService;
    }

    private HikariConfig getDataSourceProperties() {

        var dbProps = vaultOperationService.getProps(VAULT_PATH, MYSQL_SECRETES, DBProps.class);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbProps.url());
        hikariConfig.setUsername(dbProps.username);
        hikariConfig.setPassword(dbProps.password);
        return hikariConfig;
    }

    @Primary
    @Bean(name = "primaryDataSource")
    public HikariDataSource primaryDataSource() {
        return new HikariDataSource(getDataSourceProperties());
    }

    @Bean(name = "lockDataSource")
    public HikariDataSource lockDataSource() {
        return new HikariDataSource(getDataSourceProperties());
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


}
