package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import br.gov.mt.seplag.artists_api.domain.repository.ArtistaRepository;
import br.gov.mt.seplag.artists_api.mapper.ArtistaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ArtistaMapper artistaMapper;

    public ArtistaResponseDTO criar(ArtistaRequestDTO dto) {
        Artista entity = artistaMapper.toEntity(dto);
        return artistaMapper.toResponse(artistaRepository.save(entity));
    }

    public Page<ArtistaResumoDTO> listar(String nome, int page, int size, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(page, size, sort);

        if (nome != null && !nome.isBlank()) {
            return artistaRepository.findByNomeContainingIgnoreCase(nome, pageable)
                    .map(artistaMapper::toResumo);
        }

        return artistaRepository.findAll(pageable)
                .map(artistaMapper::toResumo);
    }

    public ArtistaResponseDTO buscarPorId(Long id) {
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
        return artistaMapper.toResponse(artista);
    }

    public ArtistaResponseDTO atualizar(Long id, ArtistaRequestDTO dto) {
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        artistaMapper.updateEntity(dto, artista);
        return artistaMapper.toResponse(artistaRepository.save(artista));
    }

    public void deletar(Long id) {
        artistaRepository.deleteById(id);
    }
}
