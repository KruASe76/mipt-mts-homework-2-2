package me.kruase.mipt.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.lifecycle.Startables;

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
public class TestcontainersConfig {
    private static final CassandraContainer CASSANDRA =
            new CassandraContainer("cassandra:5.0")
                    .withExposedPorts(9042)
                    .withInitScript("db/cassandra/init.cql")
                    .withReuse(true);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            Startables.deepStart(CASSANDRA).join();

            TestPropertyValues.of(
                    "spring.cassandra.contact-points=" + CASSANDRA.getHost(),
                    "spring.cassandra.port=" + CASSANDRA.getMappedPort(9042)
            ).applyTo(context);
        }
    }
}
