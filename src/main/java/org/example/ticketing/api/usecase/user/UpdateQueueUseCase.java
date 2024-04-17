package org.example.ticketing.api.usecase.user;

import org.example.ticketing.api.dto.request.UserRequestDTO;
import org.example.ticketing.api.dto.response.QueueResponseDTO;
import org.example.ticketing.domain.user.model.Queue;
import org.example.ticketing.domain.user.model.Token;
import org.example.ticketing.domain.user.service.QueueService;
import org.example.ticketing.domain.user.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UpdateQueueUseCase {
    private final TokenService tokenService;
    private final QueueService queueService;

    public UpdateQueueUseCase(TokenService tokenService, QueueService queueService) {
        this.tokenService = tokenService;
        this.queueService = queueService;
    }

    public QueueResponseDTO execute(UserRequestDTO userRequestDTO) throws Exception {

        Queue myQueue = queueService.findQueueInfo(userRequestDTO.user_id());
        Token myToken = tokenService.checkToken(userRequestDTO);
        // # 1 token table 인원 조회
        Long tokenCount = tokenService.findTokenCount();
        Long queueCount = queueService.findQueueCount(myQueue != null ? myQueue.getUpdatedAt() : LocalDateTime.now());

        if(myToken != null) {
            return new QueueResponseDTO("이미 유효토큰이 발급되어있습니다.", null, myToken.getTokenValue(), myToken.getExpiredAt());
        }

        // # 1.1 token table 10명 미만
        if(tokenCount < 10 && queueCount == 0) {
            // # 1.1.1 Queue table 조회 후 내 순번이면 token table insert / queue table delete
            String userUUID = UUID.randomUUID().toString();
//            LocalDateTime tokenExpiredTime = LocalDateTime.now().plusMinutes(1);
            LocalDateTime tokenExpiredTime = LocalDateTime.now().plusSeconds(30);
            Token tokenValue = new Token(userRequestDTO.user_id(), userUUID, tokenExpiredTime, true);
            tokenService.enterToken(tokenValue);
            if(myQueue == null) {
                return new QueueResponseDTO("유효토큰이 발급되었습니다.", null, tokenValue.getTokenValue(), tokenValue.getExpiredAt());
            }
        } else {
            if(myQueue == null){
                queueService.enterQueue(userRequestDTO.user_id());
            }
        }
        return new QueueResponseDTO("대기중입니다.", queueCount, null,null);
    }
}