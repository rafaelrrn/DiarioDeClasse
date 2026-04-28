package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.GradeCurricularDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/grades-curriculares")
@Tag(name = "GradeCurricular", description = "Grade de horários semanais fixos de uma classe (dia, número de aula, horário)")
public class GradeCurricularController {

    private final GradeCurricularService service;

    public GradeCurricularController(GradeCurricularService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as grades curriculares")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeCurricularDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar grade curricular por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeCurricularDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar grade de uma classe, ordenada por dia e número de aula")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/classe/{idClasse}")
    public ResponseEntity<ApiResponse<List<GradeCurricularDTO>>> buscarPorClasse(
            @PathVariable Long idClasse) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorClasse(idClasse)));
    }

    @Operation(summary = "Criar horário na grade curricular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<GradeCurricularDTO>> criar(@RequestBody GradeCurricularDTO dto) {
        GradeCurricular entity = new GradeCurricular();
        entity.setDiaSemana(dto.getDiaSemana());
        entity.setNumeroAula(dto.getNumeroAula());
        entity.setHorarioInicio(dto.getHorarioInicio());
        entity.setHorarioFim(dto.getHorarioFim());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.criar(entity, dto.getIdClasse()),
                        "Horário adicionado à grade curricular com sucesso"));
    }

    @Operation(summary = "Atualizar horário na grade curricular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeCurricularDTO>> atualizar(
            @PathVariable Long id, @RequestBody GradeCurricularDTO dto) {
        GradeCurricular dados = new GradeCurricular();
        dados.setDiaSemana(dto.getDiaSemana());
        dados.setNumeroAula(dto.getNumeroAula());
        dados.setHorarioInicio(dto.getHorarioInicio());
        dados.setHorarioFim(dto.getHorarioFim());
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, dados)));
    }

    @Operation(summary = "Remover horário da grade curricular (soft delete)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Horário removido da grade curricular com sucesso"));
    }
}
