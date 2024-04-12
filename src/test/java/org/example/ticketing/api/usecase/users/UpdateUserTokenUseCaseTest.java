package org.example.ticketing.api.usecase.users;

import org.example.ticketing.api.dto.request.UserRequestDTO;
import org.example.ticketing.api.dto.response.TokenResponseDTO;
import org.example.ticketing.api.usecase.user.UpdateUserTokenUseCase;
import org.example.ticketing.api.usecase.common.UpdateTokenQueueWaitInfo;
import org.example.ticketing.domain.user.model.Token;
import org.example.ticketing.domain.user.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UpdateUserTokenUseCaseTest {
    private UpdateUserTokenUseCase updateUserTokenUseCase;
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UpdateTokenQueueWaitInfo updateTokenQueueWaitInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        updateUserTokenUseCase = new UpdateUserTokenUseCase(tokenRepository, updateTokenQueueWaitInfo);
    }

    @DisplayName("유저 토큰 대기열 업데이트")
    @Test
    public void executeTest() {
        Long userId = 1L;
        UserRequestDTO userRequestDTO = new UserRequestDTO(userId);
        String expectedTokenOnWaitStatus = "onWait";
        String expectedTokenNum = "2";

        Token token = Token.builder()
                .user_id(userId)
                .token_value("abcd-efgh-ijkl/onWait/4")
                .updated_at(LocalDateTime.now())
                .created_at(LocalDateTime.now()).build();

        when(tokenRepository.findByUserId(any())).thenReturn(token);
        when(updateTokenQueueWaitInfo.execute(any(), any())).thenReturn(new TokenResponseDTO("abcd-efgh-ijkl/onWait/2"));
        TokenResponseDTO actualToken = updateUserTokenUseCase.execute(userRequestDTO);

        assertEquals(expectedTokenOnWaitStatus, actualToken.token().split("/")[1]);
        assertEquals(expectedTokenNum, actualToken.token().split("/")[2]);
    }

}