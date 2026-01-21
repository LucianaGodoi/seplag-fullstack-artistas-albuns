package br.gov.mt.seplag.artists_api.api.controller;

import br.gov.mt.seplag.artists_api.api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.artists_api.domain.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/albuns")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> criar(@RequestBody AlbumRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<AlbumResponseDTO>> listar(
            @RequestParam(required = false) Long artistaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(albumService.listar(artistaId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AlbumRequestDTO dto
    ) {
        return ResponseEntity.ok(albumService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        albumService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
