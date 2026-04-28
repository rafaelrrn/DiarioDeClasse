package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.CronogramaAnualDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cronogramas-anuais")
@Tag(name = "CronogramaAnual", description = "Gerenciamento do cronograma anual letivo da escola")
public class CronogramaAnualController {

    private final CronogramaAnualService service;

    public CronogramaAnualController(CronogramaAnualService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os cronogramas anuais ativos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CronogramaAnualDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar cronograma anual por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CronogramaAnualDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar cronogramas por status (RASCUNHO, ATIVO, ENCERRADO)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<CronogramaAnualDTO>>> buscarPorStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorStatus(status)));
    }

    @Operation(summary = "Criar cronograma anual")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CronogramaAnualDTO>> criar(@RequestBody CronogramaAnualDTO dto) {
        CronogramaAnual entity = new CronogramaAnual();
        entity.setAno(dto.getAno());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setDiasLetivosPrevistos(dto.getDiasLetivosPrevistos());
        entity.setCargaHorariaPrevista(dto.getCargaHorariaPrevista());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "RASCUNHO");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.criar(entity), "Cronograma anual criado com sucesso"));
    }

    @Operation(summary = "Atualizar cronograma anual")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CronogramaAnualDTO>> atualizar(
            @PathVariable Long id, @RequestBody CronogramaAnualDTO dto) {
        CronogramaAnual dados = new CronogramaAnual();
        dados.setAno(dto.getAno());
        dados.setDataInicio(dto.getDataInicio());
        dados.setDataFim(dto.getDataFim());
        dados.setDiasLetivosPrevistos(dto.getDiasLetivosPrevistos());
        dados.setCargaHorariaPrevista(dto.getCargaHorariaPrevista());
        dados.setStatus(dto.getStatus());
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, dados)));
    }

    @Operation(summary = "Desativar cronograma anual (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Cronograma anual desativado com sucesso"));
    }
}
