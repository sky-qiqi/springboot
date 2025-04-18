package org.example.system.repository;

import org.example.system.domain.LogisticsTrace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsTraceRepository extends MongoRepository<LogisticsTrace, String> {

    // Find all traces for a specific order, sorted by timestamp
    List<LogisticsTrace> findByOrderIdOrderByTimestampAsc(Long orderId);

    // You can add other custom query methods here if needed
}