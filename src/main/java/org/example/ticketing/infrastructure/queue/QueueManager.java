package org.example.ticketing.infrastructure.queue;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueManager {

    private final RedissonClient redissonClient;
    private final String queueName = "waitingQueue";

    public void addToQueue(Long userId) {
        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueName);
        double score = System.currentTimeMillis(); // 현재 시간을 점수로 사용하여 대기열에 추가
        queue.add(score, userId);
    }

    public Long getNextInQueue() {
        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueName);
        return queue.first();
    }

    public int getQueuePosition(Long userId) {
        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueName);
        Integer position = queue.rank(userId);
        if (position != null) {
            // 순위는 0부터 시작하므로 실제 대기열 위치를 얻기 위해 1을 더함
            return position + 1;
        } else {
            // 사용자가 대기열에 없는 경우
            return -1;
        }
    }

    public void removeUserFromQueue(Long userId) {
        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueName);
        queue.remove(userId); // 대기열에서 사용자 제거
    }
}