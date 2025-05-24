package me.kruase.mipt.services;

import me.kruase.mipt.config.TestcontainersConfig;
import me.kruase.mipt.models.OperationType;
import me.kruase.mipt.models.UserAuditData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.CassandraInvalidQueryException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TestUserAuditService extends TestcontainersConfig {
    private static final long firstUserId = 1;
    private static final long secondUserId = 2;
    private static final long nonExistingUserId = 666;

    @Autowired
    UserAuditService service;

    @Test
    public void testHappyPath() {
        Instant now = Instant.now();

        for (int timeOffset = 1; timeOffset <= 10; timeOffset++) {
            service.recordUserActivity(
                    new UserAuditData(firstUserId, now.plusSeconds(timeOffset), OperationType.CREATE, "user 1 detail " + timeOffset)
            );

            if (timeOffset % 2 == 0) {
                service.recordUserActivity(
                        new UserAuditData(secondUserId, now.plusSeconds(timeOffset), OperationType.READ, "user 2 detail " + timeOffset)
                );
            }
        }

        assertEquals(10, service.getAllUserActivity(firstUserId, 1000).size());
        assertEquals(5, service.getAllUserActivity(secondUserId, 1000).size());

        assertEquals(7, service.getAllUserActivity(firstUserId, 7).size());
        assertEquals(5, service.getAllUserActivity(secondUserId, 7).size());

        assertEquals(10, service.getUserActivityByTimeRange(firstUserId, now.minusSeconds(5), now.plusSeconds(15), 1000).size());
        assertEquals(5, service.getUserActivityByTimeRange(secondUserId, now.minusSeconds(5), now.plusSeconds(15), 1000).size());
        assertEquals(2, service.getUserActivityByTimeRange(secondUserId, now.plusSeconds(1), now.plusSeconds(5), 1000).size());
    }

    @Test
    public void testBadRequests() {
        Instant now = Instant.now();

        assertEquals(0, service.getAllUserActivity(nonExistingUserId, 1000).size());
        assertEquals(0, service.getUserActivityByTimeRange(firstUserId, now.minusSeconds(1000), now.minusSeconds(100), 1000).size());

        assertEquals(0, service.getUserActivityByTimeRange(firstUserId, now.plusSeconds(10), now.minusSeconds(10), 1000).size());

        assertThrows(
                CassandraInvalidQueryException.class,
                () -> service.getUserActivityByTimeRange(firstUserId, now, now.plusSeconds(100), 0)
        );
        assertThrows(
                CassandraInvalidQueryException.class,
                () -> service.getUserActivityByTimeRange(secondUserId, now, now.plusSeconds(100), -100)
        );

    }
}
