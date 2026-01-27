package br.gov.mt.seplag.artists_api.api.dto;

import java.time.LocalDateTime;

public record AlbumNotificationDTO(
        Long albumId,
        String albumNome,
        String artistaNome,
        LocalDateTime dataCriacao
) {}
