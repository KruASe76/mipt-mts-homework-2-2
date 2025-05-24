package me.kruase.mipt.db.audit;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.time.Instant;
import java.util.List;

public interface UserAuditRepository extends CassandraRepository<UserAudit, Long> {
    @Query("SELECT * FROM user_audit WHERE user_id = :userId LIMIT :limit")
    List<UserAudit> findAllByUserId(Long userId, int limit);

    @Query("SELECT * FROM user_audit WHERE user_id = :userId AND timestamp >= :timestampFrom AND timestamp <= :timestampTo LIMIT :limit")
    List<UserAudit> findByUserIdAndTimestampRange(Long userId, Instant timestampFrom, Instant timestampTo, int limit);
}
