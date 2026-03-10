package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.InstituicaoEnsinoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/instituicoes-ensino")
@Tag(name = "InstituicaoEnsino", description = "Gerenciamento de InstituicaoEnsino")
public class InstituicaoEnsinoController {

    private final InstituicaoEnsinoService service;
    public InstituicaoEnsinoController(InstituicaoEnsinoService service) { this.service = service; }

    @Operation(summary = "Listar todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<InstituicaoEnsinoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(InstituicaoEnsinoDTO::new).toList());
    }

    @Operation(summary = "Buscar por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsinoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new InstituicaoEnsinoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<InstituicaoEnsinoDTO> criar(@RequestBody InstituicaoEnsinoDTO dto) {
        InstituicaoEnsino entity = new InstituicaoEnsino();
        BeanUtils.copyProperties(dto, entity, "idInstituicaoEnsino");
        return ResponseEntity.status(HttpStatus.CREATED).body(new InstituicaoEnsinoDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoEnsinoDTO> atualizar(@PathVariable Long id, @RequestBody InstituicaoEnsinoDTO dto) {
        InstituicaoEnsino dados = new InstituicaoEnsino();
        BeanUtils.copyProperties(dto, dados, "idInstituicaoEnsino");
        return ResponseEntity.ok(new InstituicaoEnsinoDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
