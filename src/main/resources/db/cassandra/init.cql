CREATE KEYSPACE IF NOT EXISTS kruase_keyspace
    WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': 1};

CREATE TABLE kruase_keyspace.user_audit (
    user_id BIGINT,
    timestamp TIMESTAMP,
    operation_type TEXT,
    details TEXT,
    PRIMARY KEY ((user_id), timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);
