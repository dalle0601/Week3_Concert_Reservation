package org.example.ticketing.api.usecase.point;

import lombok.RequiredArgsConstructor;
import org.example.ticketing.api.dto.point.reqeust.PointHistorySaveRequestDTO;
import org.example.ticketing.api.dto.point.response.PointHistorySaveResponseDTO;
import org.example.ticketing.domain.point.model.PointHistory;
import org.example.ticketing.domain.point.service.PointHistoryService;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor

public class WritePointHistoryUseCase {
    private final PointHistoryService pointHistoryService;

    public PointHistorySaveResponseDTO execute(PointHistorySaveRequestDTO pointHistorySaveRequestDTO){
        try {
            PointHistory savePointHistory = pointHistoryService.save(new PointHistory(pointHistorySaveRequestDTO.userId(), pointHistorySaveRequestDTO.point(), pointHistorySaveRequestDTO.status()));
            if (savePointHistory != null) {
                return new PointHistorySaveResponseDTO("포인트 내역 저장 성공", savePointHistory);
            } else {
                // 포인트 내역이 반환되지 않을 경우
                return new PointHistorySaveResponseDTO("포인트 내역 저장 실패", null);
            }
        } catch (Exception e) {
            return new PointHistorySaveResponseDTO("포인트 내역 저장 중 오류가 발생했습니다.", null);
        }
    }
}
