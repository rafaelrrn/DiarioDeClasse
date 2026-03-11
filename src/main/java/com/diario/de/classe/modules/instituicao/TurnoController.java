package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.TurnoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/turnos")
@Tag(name = "Turno", description = "Gerenciamento de Turno")
public class TurnoController {

    private final TurnoService service;
    public TurnoController(TurnoService service) { this.service = service; }

    @Operation(summary = "Listar todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TurnoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(TurnoDTO::new).toList()));
    }

    @Operation(summary = "Buscar por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TurnoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new TurnoDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<TurnoDTO>> criar(@RequestBody TurnoDTO dto) {
        Turno entity = new Turno();
        BeanUtils.copyProperties(dto, entity, "idTurno");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new TurnoDTO(service.criar(entity)), "Turno criado com sucesso"));
    }

    @Operation(summary = "Atualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TurnoDTO>> atualizar(@PathVariable Long id, @RequestBody TurnoDTO dto) {
        Turno dados = new Turno();
        BeanUtils.copyProperties(dto, dados, "idTurno");
        return ResponseEntity.ok(ApiResponse.ok(new TurnoDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
