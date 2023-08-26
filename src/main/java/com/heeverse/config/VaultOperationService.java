package com.heeverse.config;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;

import java.util.Objects;


/**
 * @author gutenlee
 * @since 2023/08/21가
 */
@RequiredArgsConstructor
@Component
public class VaultOperationService {

    private final VaultOperations vaultOperations;

    public <T> T getProps(String path, String secrets, Class<T> clazz) {

        VaultKeyValueOperations operations = getKeyValueOperations(path);
        Assert.notNull(operations, "VaultKeyValueOperations must not null!");

        return Objects.requireNonNull(operations.get(secrets, clazz)).getData();
    }

    private VaultKeyValueOperations getKeyValueOperations(String path) {
        return vaultOperations.opsForKeyValue(path, VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);
    }
}
