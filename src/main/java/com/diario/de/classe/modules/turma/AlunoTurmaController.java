package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.AlunoTurmaDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/alunos-turma")
@Tag(name = "AlunoTurma", description = "Consulta e gestão de matrículas individuais. Para matricular via turma, use POST /v1/turmas/{id}/alunos")
public class AlunoTurmaController {

    private final AlunoTurmaService service;

    public AlunoTurmaController(AlunoTurmaService service) { this.service = service; }

    /**
     * Lista todas as matrículas ativas do sistema.
     * Registros desativados via soft delete não aparecem.
     */
    @Operation(summary = "Listar todas as matrículas ativas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlunoTurmaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(AlunoTurmaDTO::new).toList()));
    }

    /**
     * Busca uma matrícula específica pelo ID.
     */
    @Operation(summary = "Buscar matrícula por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoTurmaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoTurmaDTO(service.buscarPorId(id))));
    }

    /**
     * Matricula um aluno em uma turma.
     *
     * Aplica regra de negócio: aluno não pode estar matriculado na mesma turma
     * duas vezes simultaneamente (lança HTTP 422 se ocorrer).
     *
     * Alternativa orientada a recursos: POST /v1/turmas/{id}/alunos.
     */
    @Operation(summary = "Matricular aluno em turma (com validação de duplicidade)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AlunoTurmaDTO>> matricular(@RequestBody AlunoTurmaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(
                new AlunoTurmaDTO(service.matricular(dto.getIdAluno(), dto.getIdTurma(), dto.getObs())),
                "Aluno matriculado com sucesso"
        ));
    }

    /**
     * Desativa uma matrícula (soft delete).
     *
     * O registro permanece no banco com ativo=false para manter o histórico pedagógico.
     * Após desativar, o aluno pode ser rematriculado na mesma turma.
     */
    @Operation(summary = "Desativar matrícula (soft delete)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Matrícula desativada com sucesso"));
    }
}
