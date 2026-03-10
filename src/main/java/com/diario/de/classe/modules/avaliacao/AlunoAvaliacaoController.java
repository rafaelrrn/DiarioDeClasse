package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AlunoAvaliacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/alunos-avaliacao")
@Tag(name = "AlunoAvaliacao", description = "Gerenciamento de notas de alunos")
public class AlunoAvaliacaoController {

    private final AlunoAvaliacaoService service;

    public AlunoAvaliacaoController(AlunoAvaliacaoService service) { this.service = service; }

    @Operation(summary = "Listar todas as notas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AlunoAvaliacaoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(AlunoAvaliacaoDTO::new).toList());
    }

    @Operation(summary = "Buscar nota por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoAvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AlunoAvaliacaoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Registrar nota de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<AlunoAvaliacaoDTO> criar(@RequestBody AlunoAvaliacaoDTO dto) {
        AlunoAvaliacao entity = new AlunoAvaliacao();
        entity.setNota(dto.getNota());
        entity.setObs(dto.getObs());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AlunoAvaliacaoDTO(service.criar(entity, dto.getIdAluno(), dto.getIdAvaliacao())));
    }

    @Operation(summary = "Atualizar nota de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AlunoAvaliacaoDTO> atualizar(@PathVariable Long id, @RequestBody AlunoAvaliacaoDTO dto) {
        AlunoAvaliacao dados = new AlunoAvaliacao();
        dados.setNota(dto.getNota());
        dados.setObs(dto.getObs());
        return ResponseEntity.ok(new AlunoAvaliacaoDTO(service.atualizar(id, dados, dto.getIdAluno(), dto.getIdAvaliacao())));
    }

    @Operation(summary = "Excluir nota")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
