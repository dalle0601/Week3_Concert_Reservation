package org.example.ticketing.api.dto.response;

import java.time.LocalDateTime;

public record TokenResponseDTO (
        String message,
        String token,
        LocalDateTime expiredTime
){
}
