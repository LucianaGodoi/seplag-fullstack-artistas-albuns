package br.gov.mt.seplag.artists_api.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArtistaResponseDTO {
    private Long id;
    private String nome;
    private String tipo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AlbumResponseDTO> albuns;
}
