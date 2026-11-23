package com.amar.assistente.controller;

import com.amar.assistente.dto.MetodologiaRequest;
import com.amar.assistente.dto.MetodologiaResponse;
import com.amar.assistente.service.IaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metodologia")
public class MetodologiaController {

    private final IaService iaService;

    public MetodologiaController(IaService iaService) {
        this.iaService = iaService;
    }

    @PostMapping("/gerar")
    public ResponseEntity<MetodologiaResponse> gerar(@Valid @RequestBody MetodologiaRequest request) {
        try {
            String sugestao = iaService.gerarMetodologia(request.hiperfoco(), request.conteudo());
            return ResponseEntity.ok(MetodologiaResponse.ok(sugestao));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(MetodologiaResponse.erro("Erro ao gerar metodologia: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("A.M.A.R Backend funcionando ");
    }
}
