package me.kruase.mipt.services;

import lombok.RequiredArgsConstructor;
import me.kruase.mipt.db.audit.UserAudit;
import me.kruase.mipt.db.audit.UserAuditRepository;
import me.kruase.mipt.models.UserAuditData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuditService {
    private final @NotNull CassandraTemplate cassandraTemplate;

    private final @NotNull UserAuditRepository repository;

    public void recordUserActivity(@NotNull UserAuditData data) {
        UserAudit entity = new UserAudit(data.userId(), data.timestamp(), data.operationType(), data.details());

        cassandraTemplate.insert(entity);
    }

    public @NotNull List<UserAuditData> getAllUserActivity(long userId, int limit) {
        List<UserAudit> activity = repository.findAllByUserId(userId, limit);

        return activity.stream().map(UserAuditData::from).toList();
    }

    public @NotNull List<UserAuditData> getUserActivityByTimeRange(
            long userId, Instant timestampFrom, Instant timestampTo, int limit
    ) {
        List<UserAudit> activity = repository.findByUserIdAndTimestampRange(userId, timestampFrom, timestampTo, limit);

        return activity.stream().map(UserAuditData::from).toList();
    }
}
