package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.CursoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cursos")
@Tag(name = "Curso", description = "Gerenciamento de cursos da instituição")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) { this.service = service; }

    @Operation(summary = "Listar todos os cursos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CursoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(CursoDTO::new).toList()));
    }

    @Operation(summary = "Buscar curso por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new CursoDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar novo curso")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CursoDTO>> criar(@RequestBody CursoDTO dto) {
        Curso entity = new Curso();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new CursoDTO(service.criar(entity, dto.getIdEnsino(), dto.getIdGrau(), dto.getIdSerie())), "Curso criado com sucesso"));
    }

    @Operation(summary = "Atualizar curso")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> atualizar(@PathVariable Long id, @RequestBody CursoDTO dto) {
        Curso dados = new Curso();
        return ResponseEntity.ok(ApiResponse.ok(new CursoDTO(service.atualizar(id, dados, dto.getIdEnsino(), dto.getIdGrau(), dto.getIdSerie()))));
    }

    @Operation(summary = "Excluir curso")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
