package br.gov.mt.seplag.artists_api.api.dto;

import lombok.Data;

@Data
public class ArtistaResumoDTO {
    private Long id;
    private String nome;
    private Long totalAlbuns;
}
