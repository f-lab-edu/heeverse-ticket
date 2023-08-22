package com.heeverse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
@Profile(value = {"dev"})
public class RdsConnectionProps extends DataSourceProperties {

    @Autowired
    private VaultOperationService vaultOperationService;


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

}
