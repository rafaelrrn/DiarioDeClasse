package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.frequencia.dto.AlunoFrequenciaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/frequencias")
@Tag(name = "AlunoFrequencia", description = "Gerenciamento de frequência de alunos")
public class AlunoFrequenciaController {

    private final AlunoFrequenciaService service;

    public AlunoFrequenciaController(AlunoFrequenciaService service) { this.service = service; }

    @Operation(summary = "Listar todas as frequências")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AlunoFrequenciaDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(AlunoFrequenciaDTO::new).toList());
    }

    @Operation(summary = "Buscar frequência por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoFrequenciaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AlunoFrequenciaDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Registrar frequência de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<AlunoFrequenciaDTO> criar(@RequestBody AlunoFrequenciaDTO dto) {
        AlunoFrequencia entity = new AlunoFrequencia();
        entity.setFaltas(dto.getFaltas());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AlunoFrequenciaDTO(service.criar(entity, dto.getIdAluno(), dto.getIdCalendarioEscolar())));
    }

    @Operation(summary = "Atualizar frequência de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AlunoFrequenciaDTO> atualizar(@PathVariable Long id, @RequestBody AlunoFrequenciaDTO dto) {
        AlunoFrequencia dados = new AlunoFrequencia();
        dados.setFaltas(dto.getFaltas());
        return ResponseEntity.ok(new AlunoFrequenciaDTO(service.atualizar(id, dados, dto.getIdAluno(), dto.getIdCalendarioEscolar())));
    }

    @Operation(summary = "Excluir frequência")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
