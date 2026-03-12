package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.frequencia.dto.AlunoFrequenciaDTO;
import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller de frequência de alunos.
 *
 * <p>Rotas disponíveis:
 * <ul>
 *   <li>{@code GET /v1/frequencias} — lista todos os registros ativos.</li>
 *   <li>{@code GET /v1/frequencias/{id}} — busca por ID.</li>
 *   <li>{@code GET /v1/frequencias/aluno/{idAluno}} — histórico de frequência do aluno.</li>
 *   <li>{@code GET /v1/frequencias/aluno/{idAluno}/resumo} — percentual LDB e risco de reprovação.</li>
 *   <li>{@code POST /v1/frequencias} — lança frequência de um aluno em uma aula.</li>
 *   <li>{@code POST /v1/frequencias/turma/{idTurma}/calendario/{idCalendario}} — lança em lote para toda a turma.</li>
 *   <li>{@code PUT /v1/frequencias/{id}} — corrige o tipo de frequência.</li>
 *   <li>{@code DELETE /v1/frequencias/{id}} — desativa (soft delete).</li>
 * </ul>
 */
@RestController
@RequestMapping("/v1/frequencias")
@Tag(name = "Frequência", description = "Lançamento e consulta de frequência de alunos (LDB Art. 24 — mínimo 75%)")
public class AlunoFrequenciaController {

    private final AlunoFrequenciaService service;

    public AlunoFrequenciaController(AlunoFrequenciaService service) {
        this.service = service;
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    /**
     * Lista todos os registros de frequência ativos no sistema.
     * Registros desativados via soft delete não aparecem.
     */
    @Operation(summary = "Listar todas as frequências ativas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AlunoFrequenciaDTO::new).toList()
        ));
    }

    /**
     * Busca um registro de frequência pelo ID.
     *
     * @param id ID do registro
     * @return frequência encontrada ou 404
     */
    @Operation(summary = "Buscar frequência por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoFrequenciaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoFrequenciaDTO(service.buscarPorId(id))));
    }

    /**
     * Lista o histórico de frequência de um aluno específico.
     *
     * @param idAluno ID da Pessoa com tipo ALUNO
     * @return lista de registros ativos do aluno
     */
    @Operation(summary = "Listar frequências de um aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> listarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarPorAluno(idAluno).stream().map(AlunoFrequenciaDTO::new).toList()
        ));
    }

    /**
     * Calcula e retorna o resumo de frequência de um aluno.
     *
     * <p>Inclui: total de aulas, total de presenças/faltas/justificadas,
     * percentual de presença e indicador de risco de reprovação (< 75% LDB).
     *
     * @param idAluno ID do aluno
     * @return resumo com percentual calculado
     */
    @Operation(summary = "Calcular resumo de frequência do aluno (percentual LDB)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/aluno/{idAluno}/resumo")
    public ResponseEntity<ApiResponse<FrequenciaResumoDTO>> calcularResumo(@PathVariable Long idAluno) {
        return ResponseEntity.ok(ApiResponse.ok(service.calcularFrequencia(idAluno)));
    }

    // -------------------------------------------------------------------------
    // Lançamento de frequência
    // -------------------------------------------------------------------------

    /**
     * Registra a frequência de um único aluno em uma aula.
     *
     * <p>Aplica regra de duplicidade: um aluno não pode ter dois registros ativos
     * para a mesma aula. Retorna HTTP 422 se isso ocorrer.
     *
     * <p>Exemplo de corpo:
     * <pre>{@code
     * { "idAluno": 1, "idCalendarioEscolar": 5, "tipoFrequencia": "FALTA" }
     * }</pre>
     *
     * @param dto DTO com idAluno, idCalendarioEscolar e tipoFrequencia (opcional, padrão PRESENTE)
     * @return registro criado
     */
    @Operation(summary = "Registrar frequência de um aluno em uma aula")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AlunoFrequenciaDTO>> registrar(@RequestBody AlunoFrequenciaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(
                new AlunoFrequenciaDTO(service.registrar(dto.getIdAluno(), dto.getIdCalendarioEscolar(), dto.getTipoFrequencia())),
                "Frequência registrada com sucesso"
        ));
    }

    /**
     * Lança frequência em lote para todos os alunos matriculados em uma turma.
     *
     * <p>Útil para o professor registrar a chamada de uma aula inteira. Alunos já com
     * frequência registrada para esta aula são ignorados (sem erro).
     *
     * <p>O corpo é um mapa opcional de {@code idAluno → TipoFrequencia}.
     * Alunos não incluídos no mapa recebem o {@code tipoPadrao} (query param, padrão: PRESENTE).
     *
     * <p>Exemplo:
     * <pre>{@code
     * POST /v1/frequencias/turma/3/calendario/10?tipoPadrao=PRESENTE
     * Body: { "2": "FALTA", "5": "FALTA_JUSTIFICADA" }
     * }</pre>
     *
     * @param idTurma      ID da turma
     * @param idCalendario ID do calendário escolar (aula)
     * @param tipoPadrao   tipo para alunos não mapeados (padrão: PRESENTE)
     * @param tiposPorAluno mapa de idAluno → TipoFrequencia (pode ser vazio)
     * @return lista de registros criados neste lançamento
     */
    @Operation(summary = "Lançar frequência em lote para toda a turma")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping("/turma/{idTurma}/calendario/{idCalendario}")
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> registrarTurma(
            @PathVariable Long idTurma,
            @PathVariable Long idCalendario,
            @RequestParam(defaultValue = "PRESENTE") TipoFrequencia tipoPadrao,
            @RequestBody(required = false) Map<Long, TipoFrequencia> tiposPorAluno) {

        List<AlunoFrequenciaDTO> resultado = service
                .registrarTurma(idTurma, idCalendario, tiposPorAluno, tipoPadrao)
                .stream()
                .map(AlunoFrequenciaDTO::new)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(
                resultado,
                "Frequência lançada para " + resultado.size() + " aluno(s)"
        ));
    }

    /**
     * Corrige o tipo de frequência de um registro existente.
     *
     * <p>Permite ao professor corrigir um lançamento errado
     * (ex: marcou FALTA mas o aluno estava PRESENTE).
     *
     * <p>Corpo: {@code { "tipoFrequencia": "PRESENTE" }}
     *
     * @param id  ID do registro a corrigir
     * @param dto DTO com o novo tipoFrequencia
     * @return registro atualizado
     */
    @Operation(summary = "Corrigir tipo de frequência de um registro")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoFrequenciaDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody AlunoFrequenciaDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(
                new AlunoFrequenciaDTO(service.atualizar(id, dto.getTipoFrequencia()))
        ));
    }

    /**
     * Desativa um registro de frequência (soft delete).
     *
     * <p>O registro permanece no banco com {@code ativo = false} para preservar
     * o histórico. Após desativar, o lançamento pode ser refeito via POST.
     *
     * @param id ID do registro a desativar
     */
    @Operation(summary = "Desativar registro de frequência (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Frequência desativada com sucesso"));
    }
}
