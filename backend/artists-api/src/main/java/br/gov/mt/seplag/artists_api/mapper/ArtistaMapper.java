package br.gov.mt.seplag.artists_api.mapper;

import br.gov.mt.seplag.artists_api.api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtistaMapper {

    Artista toEntity(ArtistaRequestDTO dto);

    ArtistaResponseDTO toResponse(Artista entity);

    ArtistaResumoDTO toResumo(Artista entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ArtistaRequestDTO dto, @MappingTarget Artista entity);
}
