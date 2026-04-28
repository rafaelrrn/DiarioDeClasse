package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.ClasseCronogramaDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/classes-cronograma")
@Tag(name = "ClasseCronograma", description = "Vínculo entre Classe e CronogramaAnual")
public class ClasseCronogramaController {

    private final ClasseCronogramaService service;

    public ClasseCronogramaController(ClasseCronogramaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os vínculos classe-cronograma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClasseCronogramaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar vínculo por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClasseCronogramaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar cronogramas vinculados a uma classe")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/classe/{idClasse}")
    public ResponseEntity<ApiResponse<List<ClasseCronogramaDTO>>> buscarPorClasse(
            @PathVariable Long idClasse) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorClasse(idClasse)));
    }

    @Operation(summary = "Listar classes vinculadas a um cronograma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/cronograma/{idCronograma}")
    public ResponseEntity<ApiResponse<List<ClasseCronogramaDTO>>> buscarPorCronograma(
            @PathVariable Long idCronograma) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorCronograma(idCronograma)));
    }

    @Operation(summary = "Vincular classe a um cronograma anual",
               description = "Corpo: { \"idClasse\": 1, \"idCronogramaAnual\": 2 }")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClasseCronogramaDTO>> vincular(
            @RequestBody ClasseCronogramaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        service.vincular(dto.getIdClasse(), dto.getIdCronogramaAnual()),
                        "Classe vinculada ao cronograma com sucesso"));
    }

    @Operation(summary = "Desvincular classe do cronograma (soft delete)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desvincular(@PathVariable Long id) {
        service.desvincular(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Vínculo desativado com sucesso"));
    }
}
