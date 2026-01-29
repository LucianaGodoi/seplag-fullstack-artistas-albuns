package br.gov.mt.seplag.artists_api.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlbumResponseDTO {
    private Long id;
    private String nome;
    private Integer anoLancamento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<AlbumCoverResponseDTO> capas;
}
