package com.heeverse.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.heeverse.common.Constants.MYSQL_SECRETES;
import static com.heeverse.common.Constants.VAULT_PATH;

/**
 * @author jeongheekim
 * @date 10/31/23
 */
@Configuration
@MultiDataSourceProfile
public class MultiDataSource {
    private final VaultOperationService vaultOperationService;

    public MultiDataSource(VaultOperationService vaultOperationService) {
        this.vaultOperationService = vaultOperationService;
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

    private HikariConfig getDataSourceProperties() {
        var dbProps = vaultOperationService.getProps(VAULT_PATH, MYSQL_SECRETES, RdsConnectionProps.DBProps.class);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbProps.url());
        hikariConfig.setUsername(dbProps.username());
        hikariConfig.setPassword(dbProps.password());
        return hikariConfig;
    }

}
