package br.gov.mt.seplag.artists_api.api.dto;

import lombok.Data;

@Data
public class AlbumRequestDTO {
    private String nome;
    private Integer anoLancamento;
    private Long artistaId;
}
