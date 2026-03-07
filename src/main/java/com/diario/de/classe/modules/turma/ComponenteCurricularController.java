package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.ComponenteCurricularDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/componentes-curriculares")
@Tag(name = "ComponenteCurricular", description = "Gerenciamento de componentes curriculares")
public class ComponenteCurricularController {

    private final ComponenteCurricularService service;

    public ComponenteCurricularController(ComponenteCurricularService service) { this.service = service; }

    @Operation(summary = "Listar todos os componentes curriculares")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<ComponenteCurricularDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(ComponenteCurricularDTO::new).toList());
    }

    @Operation(summary = "Buscar componente curricular por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ComponenteCurricularDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ComponenteCurricularDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar novo componente curricular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ComponenteCurricularDTO> criar(@RequestBody ComponenteCurricularDTO dto) {
        ComponenteCurricular entity = new ComponenteCurricular();
        BeanUtils.copyProperties(dto, entity, "idComponenteCurricular");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ComponenteCurricularDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar componente curricular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ComponenteCurricularDTO> atualizar(@PathVariable Long id, @RequestBody ComponenteCurricularDTO dto) {
        ComponenteCurricular dados = new ComponenteCurricular();
        BeanUtils.copyProperties(dto, dados, "idComponenteCurricular");
        return ResponseEntity.ok(new ComponenteCurricularDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir componente curricular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
