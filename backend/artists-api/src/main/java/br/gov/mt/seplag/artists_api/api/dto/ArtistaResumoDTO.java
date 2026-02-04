package br.gov.mt.seplag.artists_api.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaResumoDTO {

    private Long id;
    private String nome;
    private Long totalAlbuns;

}