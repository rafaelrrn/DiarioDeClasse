package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.AlunoTurmaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/alunos-turma")
@Tag(name = "AlunoTurma", description = "Gerenciamento de alunos por turma")
public class AlunoTurmaController {

    private final AlunoTurmaService service;

    public AlunoTurmaController(AlunoTurmaService service) { this.service = service; }

    @Operation(summary = "Listar todos os alunos por turma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AlunoTurmaDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(AlunoTurmaDTO::new).toList());
    }

    @Operation(summary = "Buscar aluno-turma por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoTurmaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AlunoTurmaDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Adicionar aluno à turma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<AlunoTurmaDTO> criar(@RequestBody AlunoTurmaDTO dto) {
        AlunoTurma entity = new AlunoTurma();
        entity.setObs(dto.getObs());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AlunoTurmaDTO(service.criar(entity, dto.getIdAluno(), dto.getIdTurma())));
    }

    @Operation(summary = "Atualizar aluno-turma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AlunoTurmaDTO> atualizar(@PathVariable Long id, @RequestBody AlunoTurmaDTO dto) {
        AlunoTurma dados = new AlunoTurma();
        BeanUtils.copyProperties(dto, dados, "idAlunoTurma");
        return ResponseEntity.ok(new AlunoTurmaDTO(service.atualizar(id, dados, dto.getIdAluno(), dto.getIdTurma())));
    }

    @Operation(summary = "Remover aluno da turma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
