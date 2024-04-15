package org.example.ticketing.domain.user.repository;

import org.example.ticketing.api.dto.response.QueueWaitInfoResponseDTO;
import org.example.ticketing.domain.user.model.Queue;
import org.example.ticketing.domain.user.model.projection.QueueWaitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {
    @Query("SELECT COALESCE(SUM(CASE WHEN status='onGoing' THEN 1 ELSE 0 END), 0) as onGoing, " +
            "COALESCE(SUM(CASE WHEN status='onWait' THEN 1 ELSE 0 END), 0) as onWait FROM Queue")
    QueueWaitInfo getQueueOngoingAndWait();

    @Query("SELECT q FROM Queue q WHERE q.userId = :userId")
    Optional<Queue> findByUserId(@Param("userId") Long userId);
}
