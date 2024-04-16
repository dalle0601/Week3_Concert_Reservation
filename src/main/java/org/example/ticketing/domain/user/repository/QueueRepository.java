package org.example.ticketing.domain.user.repository;

import org.example.ticketing.api.dto.response.QueueWaitInfoResponseDTO;
import org.example.ticketing.domain.user.model.Queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Optional<Queue> findByUserId(Long userId);
    Queue enterQueue(Long userId);
    Long findQueueCount();
    void deleteQueue(Long userId) throws Exception;
}
