package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.EnsinoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/ensinos")
@Tag(name = "Ensino", description = "Gerenciamento de Ensino")
public class EnsinoController {

    private final EnsinoService service;
    public EnsinoController(EnsinoService service) { this.service = service; }

    @Operation(summary = "Listar Todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<EnsinoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(EnsinoDTO::new).toList());
    }

    @Operation(summary = "Buscar por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EnsinoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new EnsinoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<EnsinoDTO> criar(@RequestBody EnsinoDTO dto) {
        Ensino entity = new Ensino();
        BeanUtils.copyProperties(dto, entity, "idEnsino");
        return ResponseEntity.status(HttpStatus.CREATED).body(new EnsinoDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<EnsinoDTO> atualizar(@PathVariable Long id, @RequestBody EnsinoDTO dto) {
        Ensino dados = new Ensino();
        BeanUtils.copyProperties(dto, dados, "idEnsino");
        return ResponseEntity.ok(new EnsinoDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
