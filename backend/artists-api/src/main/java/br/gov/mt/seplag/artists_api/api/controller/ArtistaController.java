package br.gov.mt.seplag.artists_api.api.controller;

import br.gov.mt.seplag.artists_api.api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.artists_api.domain.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService artistaService;

    @PostMapping
    public ResponseEntity<ArtistaResponseDTO> criar(@RequestBody ArtistaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ArtistaResumoDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort
    ) {
        return ResponseEntity.ok(artistaService.listar(nome, page, size, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(artistaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ArtistaRequestDTO dto
    ) {
        return ResponseEntity.ok(artistaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        artistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
