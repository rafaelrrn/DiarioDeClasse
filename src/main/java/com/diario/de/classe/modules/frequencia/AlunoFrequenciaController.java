package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.frequencia.dto.AlunoFrequenciaDTO;
import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 *   <li>{@code POST /v1/frequencias/turma/{idTurma}/aula/{idAula}} — lança em lote para toda a turma.</li>
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

    @Operation(summary = "Listar todas as frequências ativas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AlunoFrequenciaDTO::new).toList()
        ));
    }

    @Operation(summary = "Buscar frequência por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoFrequenciaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoFrequenciaDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Listar frequências de um aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> listarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarPorAluno(idAluno).stream().map(AlunoFrequenciaDTO::new).toList()
        ));
    }

    @Operation(summary = "Calcular resumo de frequência do aluno (percentual LDB)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/aluno/{idAluno}/resumo")
    public ResponseEntity<ApiResponse<FrequenciaResumoDTO>> calcularResumo(@PathVariable Long idAluno) {
        return ResponseEntity.ok(ApiResponse.ok(service.calcularFrequencia(idAluno)));
    }

    /**
     * Registra a frequência de um único aluno em uma aula.
     *
     * <p>Exemplo de corpo:
     * <pre>{@code
     * { "idAluno": 1, "idAula": 5, "tipoFrequencia": "FALTA" }
     * }</pre>
     */
    @Operation(summary = "Registrar frequência de um aluno em uma aula")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AlunoFrequenciaDTO>> registrar(@RequestBody AlunoFrequenciaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(
                new AlunoFrequenciaDTO(service.registrar(dto.getIdAluno(), dto.getIdAula(), dto.getTipoFrequencia())),
                "Frequência registrada com sucesso"
        ));
    }

    /**
     * Lança frequência em lote para todos os alunos matriculados em uma turma.
     *
     * <p>O corpo é um mapa opcional de {@code idAluno → tipoFrequencia} (String).
     * Alunos não incluídos no mapa recebem o {@code tipoPadrao} (query param, padrão: PRESENTE).
     *
     * <p>Exemplo:
     * <pre>{@code
     * POST /v1/frequencias/turma/3/aula/10?tipoPadrao=PRESENTE
     * Body: { "2": "FALTA", "5": "FALTA_JUSTIFICADA" }
     * }</pre>
     */
    @Operation(summary = "Lançar frequência em lote para toda a turma")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Frequência lançada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turma ou aula não encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Chamada encerrada ou nenhum aluno matriculado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Perfil sem permissão (requer ADMINISTRADOR ou PROFESSOR)")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping("/turma/{idTurma}/aula/{idAula}")
    public ResponseEntity<ApiResponse<List<AlunoFrequenciaDTO>>> registrarTurma(
            @PathVariable Long idTurma,
            @PathVariable Long idAula,
            @RequestParam(defaultValue = "PRESENTE") String tipoPadrao,
            @RequestBody(required = false) Map<Long, String> tiposPorAluno) {

        List<AlunoFrequenciaDTO> resultado = service
                .registrarTurma(idTurma, idAula, tiposPorAluno, tipoPadrao)
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
     * <p>Corpo: {@code { "tipoFrequencia": "PRESENTE" }}
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

    @Operation(summary = "Desativar registro de frequência (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Frequência desativada com sucesso"));
    }
}
