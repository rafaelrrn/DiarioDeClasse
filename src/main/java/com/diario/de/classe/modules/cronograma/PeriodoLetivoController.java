package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.PeriodoLetivoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/periodos-letivos")
@Tag(name = "PeriodoLetivo", description = "Gerenciamento de períodos letivos (bimestres, trimestres, semestres)")
public class PeriodoLetivoController {

    private final PeriodoLetivoService service;

    public PeriodoLetivoController(PeriodoLetivoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os períodos letivos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PeriodoLetivoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar período letivo por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PeriodoLetivoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar períodos de um cronograma, ordenados pela sequência")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/cronograma/{idCronograma}")
    public ResponseEntity<ApiResponse<List<PeriodoLetivoDTO>>> buscarPorCronograma(
            @PathVariable Long idCronograma) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorCronograma(idCronograma)));
    }

    @Operation(summary = "Criar período letivo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<PeriodoLetivoDTO>> criar(@RequestBody PeriodoLetivoDTO dto) {
        PeriodoLetivo entity = new PeriodoLetivo();
        entity.setNome(dto.getNome());
        entity.setTipo(dto.getTipo());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setOrdem(dto.getOrdem());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.criar(entity, dto.getIdCronogramaAnual()),
                        "Período letivo criado com sucesso"));
    }

    @Operation(summary = "Atualizar período letivo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PeriodoLetivoDTO>> atualizar(
            @PathVariable Long id, @RequestBody PeriodoLetivoDTO dto) {
        PeriodoLetivo dados = new PeriodoLetivo();
        dados.setNome(dto.getNome());
        dados.setTipo(dto.getTipo());
        dados.setDataInicio(dto.getDataInicio());
        dados.setDataFim(dto.getDataFim());
        dados.setOrdem(dto.getOrdem());
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, dados)));
    }

    @Operation(summary = "Desativar período letivo (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Período letivo desativado com sucesso"));
    }
}
