package me.kruase.mipt.models;

import me.kruase.mipt.db.audit.UserAudit;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public record UserAuditData(Long userId, Instant timestamp, OperationType operationType, String details) {
    public static @NotNull UserAuditData from(@NotNull UserAudit userAudit) {
        return new UserAuditData(
                userAudit.getUserId(),
                userAudit.getTimestamp(),
                userAudit.getOperationType(),
                userAudit.getDetails()
        );
    }
}
