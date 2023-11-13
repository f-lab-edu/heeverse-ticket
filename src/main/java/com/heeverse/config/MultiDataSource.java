package com.heeverse.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.Arrays;

import static com.heeverse.common.Constants.*;

/**
 * @author jeongheekim
 * @date 10/31/23
 */
@Slf4j
@Configuration
public class MultiDataSource {
    private final VaultOperationService vaultOperationService;
    private final Environment environment;

    public MultiDataSource(VaultOperationService vaultOperationService, Environment environment) {
        this.vaultOperationService = vaultOperationService;
        this.environment = environment;
    }

    @Primary
    @Bean(name = "primaryDataSource")
    public HikariDataSource primaryDataSource() {
        HikariConfig hikariConfig = getDataSourceProperties();
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "lockDataSource")
    public HikariDataSource lockDataSource() {
        HikariConfig hikariConfig = getDataSourceProperties();
        return new HikariDataSource(hikariConfig);
    }

    private HikariConfig getDataSourceProperties() {
        String[] activeProfiles = environment.getActiveProfiles();
        HikariConfig hikariConfig = new HikariConfig();

        if (Arrays.asList(activeProfiles).contains(LOCAL)) {
            hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
            hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
            hikariConfig.setUsername(environment.getProperty("spring.datasource.username"));
        } else {
            var dbProps = vaultOperationService.getProps(VAULT_PATH, MYSQL_SECRETES, DBProps.class);
            hikariConfig.setJdbcUrl(dbProps.url());
            hikariConfig.setUsername(dbProps.username());
            hikariConfig.setPassword(dbProps.password());
        }
        return hikariConfig;
    }


    public record DBProps(
            String url,
            String username,
            String password
    ) {
        public DBProps {
            String name = this.getClass().getSimpleName();
            Assert.notNull(url, name + "'s [url] must not null!");
            Assert.notNull(username, name + "'s [username] must not null!");
            Assert.notNull(password, name + "'s [password] must not null!");
        }
    }

}
