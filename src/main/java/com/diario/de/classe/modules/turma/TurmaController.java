package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.TurmaDTO;
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
@RequestMapping("/v1/turmas")
@Tag(name = "Turma", description = "Gerenciamento de turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) { this.service = service; }

    @Operation(summary = "Listar todas as turmas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TurmaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(TurmaDTO::new).toList()));
    }

    @Operation(summary = "Buscar turma por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TurmaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new TurmaDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar nova turma")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<TurmaDTO>> criar(@RequestBody TurmaDTO dto) {
        Turma entity = new Turma();
        BeanUtils.copyProperties(dto, entity, "idTurma");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new TurmaDTO(service.criar(entity)), "Turma criada com sucesso"));
    }

    @Operation(summary = "Atualizar turma")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TurmaDTO>> atualizar(@PathVariable Long id, @RequestBody TurmaDTO dto) {
        Turma dados = new Turma();
        BeanUtils.copyProperties(dto, dados, "idTurma");
        return ResponseEntity.ok(ApiResponse.ok(new TurmaDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir turma")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
