package com.heeverse.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

/**
 * @author jeongheekim
 * @date 2023/08/03
 */
@Configuration
public class VaultEnvironmentConfig extends AbstractVaultConfiguration {

    @Value("${vault.scheme}")
    private String scheme;

    @Value("${vault.uri}")
    private String url;

    @Value("${vault.token}")
    private String token;

    @Value("${vault.port}")
    private int port;

    @Override
    public VaultEndpoint vaultEndpoint() {
        String authority = url + ":" + port;
        URI uri = null;
        try {
            uri = new URI(scheme, authority, null, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return VaultEndpoint.from(uri);
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(token);
    }
}
