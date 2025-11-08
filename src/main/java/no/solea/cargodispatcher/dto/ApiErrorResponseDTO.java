package no.solea.cargodispatcher.dto;

import java.time.LocalDateTime;

public record ApiErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String errorMessage,
        String path
) {
}
