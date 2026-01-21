package br.gov.mt.seplag.artists_api.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlbumResponseDTO {
    private Long id;
    private String nome;
    private Integer anoLancamento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
