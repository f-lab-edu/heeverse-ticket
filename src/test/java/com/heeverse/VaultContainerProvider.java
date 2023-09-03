package com.heeverse;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.vault.VaultContainer;

/**
 * @author gutenlee
 * @since 2023/09/02
 */

@ActiveProfiles(profiles = {"local"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = VaultContainerProvider.class)
@Testcontainers
public class VaultContainerProvider {

    private static final String TEST_TOKEN = "aaaaabbbbbcccccc";

    @BeforeAll
    static void beforeAll() {
        VAULT_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        VAULT_CONTAINER.close();
    }

    @Container
    public static org.testcontainers.vault.VaultContainer<?> VAULT_CONTAINER = new VaultContainer<>("hashicorp/vault:1.13")
            .withVaultToken(TEST_TOKEN)
            .withExposedPorts(8200)
            .withInitCommand("secrets enable -path=heeverse kv", "kv put -mount=heeverse Authentication JWT_KEY=0ad48c81935843ef3390fd7490a90a3a14714f6e0e65640a2d22108264105ba2")
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig()
                            .withPortBindings(new PortBinding(Ports.Binding.bindPort(8200), new ExposedPort(8200)))
            ));

    @DynamicPropertySource
    public static void registerVaultProperties(DynamicPropertyRegistry registry) {
        registry.add("vault.uri", () -> VAULT_CONTAINER.getHost());
        registry.add("vault.port", () -> VAULT_CONTAINER.getFirstMappedPort());
        registry.add("vault.token", () -> TEST_TOKEN);
    }

}
