package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.modules.avaliacao.dto.BoletimResponseDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de consulta de notas e boletim de um aluno.
 *
 * <p>Expõe os endpoints aluno-cêntricos definidos em INSTRUCTIONS.md:
 * <ul>
 *   <li>{@code GET /v1/alunos/{id}/notas} — todas as notas ativas do aluno</li>
 *   <li>{@code GET /v1/alunos/{id}/boletim} — boletim completo com médias e frequência</li>
 * </ul>
 */
@RestController
@RequestMapping("/v1/alunos")
@Tag(name = "AlunoNotas", description = "Notas e boletim por aluno")
public class AlunoNotasController {

    private final AlunoAvaliacaoService service;

    public AlunoNotasController(AlunoAvaliacaoService service) {
        this.service = service;
    }

    /**
     * Lista todas as notas ativas de um aluno.
     *
     * <p>Retorna os registros de AlunoAvaliacao com nota, observação e identificação
     * da avaliação. Para visualizar médias calculadas, use o boletim.
     *
     * @param id ID do aluno (Pessoa)
     * @return lista de notas do aluno
     */
    @Operation(
            summary = "Listar notas do aluno",
            description = "Retorna todas as notas ativas registradas para o aluno. " +
                          "Para médias e situação por disciplina, use GET /v1/alunos/{id}/boletim."
    )
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR', 'ALUNO', 'RESPONSAVEL')")
    @GetMapping("/{id}/notas")
    public ResponseEntity<ApiResponse<List<AlunoAvaliacaoDTO>>> listarNotas(@PathVariable Long id) {
        List<AlunoAvaliacaoDTO> notas = service.buscarPorAluno(id)
                .stream().map(AlunoAvaliacaoDTO::new).toList();
        return ResponseEntity.ok(ApiResponse.ok(notas));
    }

    /**
     * Gera o boletim completo de um aluno.
     *
     * <p>Consolida em uma única resposta:
     * <ul>
     *   <li>Resumo de frequência: total de aulas, presenças, faltas e percentual (LDB 75%).</li>
     *   <li>Média ponderada por disciplina: {@code Σ(nota * peso) / Σ(peso)}.</li>
     *   <li>Situação em cada disciplina: APROVADO, EM_RECUPERACAO, REPROVADO_NOTA
     *       ou REPROVADO_FREQUENCIA.</li>
     * </ul>
     *
     * @param id ID do aluno (Pessoa)
     * @return boletim com frequência e médias por disciplina
     */
    @Operation(
            summary = "Boletim do aluno",
            description = """
                    Retorna o boletim completo do aluno com:
                    - Frequência global (percentual e risco LDB 75%)
                    - Média ponderada por disciplina (fórmula: Σ(nota * peso) / Σ(peso))
                    - Situação: APROVADO, EM_RECUPERACAO, REPROVADO_NOTA ou REPROVADO_FREQUENCIA
                    """
    )
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR', 'ALUNO', 'RESPONSAVEL')")
    @GetMapping("/{id}/boletim")
    public ResponseEntity<ApiResponse<BoletimResponseDTO>> boletim(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.gerarBoletim(id)));
    }
}
