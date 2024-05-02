package org.example.ticketing.api.usecase.user;

import lombok.RequiredArgsConstructor;
import org.example.ticketing.api.dto.user.request.UserRequestDTO;
import org.example.ticketing.api.dto.user.response.QueueResponseDTO;
import org.example.ticketing.api.dto.user.response.TokenResponseDTO;
import org.example.ticketing.domain.user.model.Token;
import org.example.ticketing.domain.user.service.TokenService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckTokenUseCase {
    private final TokenService tokenService;
    private final UpdateQueueUseCase updateQueueUseCase;

    public TokenResponseDTO execute(UserRequestDTO userRequestDTO) throws Exception {
        Token token = tokenService.checkToken(userRequestDTO);
        if(token == null) {
            QueueResponseDTO queueResponseDTO = updateQueueUseCase.execute(userRequestDTO);
            return new TokenResponseDTO("토큰이 확인되지 않습니다.", null, null);
        } else {
            return new TokenResponseDTO("유효한 토큰입니다.", token.getTokenValue(), token.getExpiredAt());
        }

    }
}
