package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.DisciplinaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/disciplinas")
@Tag(name = "Disciplina", description = "Gerenciamento de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) { this.service = service; }

    @Operation(summary = "Listar todas as disciplinas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(DisciplinaDTO::new).toList());
    }

    @Operation(summary = "Buscar disciplina por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new DisciplinaDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar nova disciplina")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<DisciplinaDTO> criar(@RequestBody DisciplinaDTO dto) {
        Disciplina entity = new Disciplina();
        BeanUtils.copyProperties(dto, entity, "idDisciplina");
        return ResponseEntity.status(HttpStatus.CREATED).body(new DisciplinaDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar disciplina")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> atualizar(@PathVariable Long id, @RequestBody DisciplinaDTO dto) {
        Disciplina dados = new Disciplina();
        BeanUtils.copyProperties(dto, dados, "idDisciplina");
        return ResponseEntity.ok(new DisciplinaDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir disciplina")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
