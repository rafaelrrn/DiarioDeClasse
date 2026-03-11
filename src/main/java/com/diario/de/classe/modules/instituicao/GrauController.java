package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.GrauDTO;
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
@RequestMapping("/v1/graus")
@Tag(name = "Grau", description = "Gerenciamento de Grau")
public class GrauController {

    private final GrauService service;
    public GrauController(GrauService service) { this.service = service; }

    @Operation(summary = "Listar todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<GrauDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(GrauDTO::new).toList()));
    }

    @Operation(summary = "Buscar por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GrauDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new GrauDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<GrauDTO>> criar(@RequestBody GrauDTO dto) {
        Grau entity = new Grau();
        BeanUtils.copyProperties(dto, entity, "idGrau");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new GrauDTO(service.criar(entity)), "Grau criado com sucesso"));
    }

    @Operation(summary = "Atualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GrauDTO>> atualizar(@PathVariable Long id, @RequestBody GrauDTO dto) {
        Grau dados = new Grau();
        BeanUtils.copyProperties(dto, dados, "idGrau");
        return ResponseEntity.ok(ApiResponse.ok(new GrauDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
