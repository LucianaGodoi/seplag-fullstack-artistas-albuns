package br.gov.mt.seplag.artists_api.api.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType
) {}
