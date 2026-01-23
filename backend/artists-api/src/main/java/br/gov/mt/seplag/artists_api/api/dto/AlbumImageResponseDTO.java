package br.gov.mt.seplag.artists_api.api.dto;

public record AlbumImageResponseDTO(
        Long id,
        String originalFilename,
        String contentType,
        String url
) {}
