package br.gov.mt.seplag.artists_api.api.controller;

import br.gov.mt.seplag.artists_api.domain.service.RegionalSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/regionais")
@RequiredArgsConstructor
public class RegionalController {

    private final RegionalSyncService service;

    @PostMapping("/sync")
    public void sincronizar() {
        service.sincronizar();
    }
}
