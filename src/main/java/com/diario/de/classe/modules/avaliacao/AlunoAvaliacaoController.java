package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de gerenciamento direto de registros de nota (AlunoAvaliacao).
 *
 * <p>Para lançamento em lote use {@code POST /v1/avaliacoes/{id}/notas}.
 * Para consultar notas e boletim de um aluno use {@code GET /v1/alunos/{id}/notas}
 * e {@code GET /v1/alunos/{id}/boletim}.
 */
@RestController
@RequestMapping("/v1/alunos-avaliacao")
@Tag(name = "AlunoAvaliacao", description = "Gerenciamento de registros individuais de nota")
public class AlunoAvaliacaoController {

    private final AlunoAvaliacaoService service;

    public AlunoAvaliacaoController(AlunoAvaliacaoService service) {
        this.service = service;
    }

    /**
     * Lista todas as notas ativas.
     *
     * @return lista de notas com status HTTP 200
     */
    @Operation(summary = "Listar todas as notas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlunoAvaliacaoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AlunoAvaliacaoDTO::new).toList()));
    }

    /**
     * Busca uma nota pelo ID.
     *
     * @param id ID do registro de nota
     * @return nota encontrada
     */
    @Operation(summary = "Buscar nota por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoAvaliacaoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoAvaliacaoDTO(service.buscarPorId(id))));
    }

    /**
     * Registra a nota de um único aluno em uma avaliação.
     *
     * <p>Para lançar notas de vários alunos de uma vez, prefira
     * {@code POST /v1/avaliacoes/{id}/notas}.
     *
     * @param dto dados da nota (idAluno, idAvaliacao, nota, obs)
     * @return nota criada com status HTTP 201
     */
    @Operation(summary = "Registrar nota de um aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AlunoAvaliacaoDTO>> registrar(@RequestBody AlunoAvaliacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        new AlunoAvaliacaoDTO(service.registrar(dto.getIdAluno(), dto.getIdAvaliacao(),
                                dto.getNota(), dto.getObs())),
                        "Nota registrada com sucesso"));
    }

    /**
     * Atualiza a nota de um registro existente.
     *
     * <p>Permite corrigir uma nota lançada incorretamente.
     *
     * @param id  ID do registro de nota
     * @param dto novos dados (nota, obs)
     * @return nota atualizada
     */
    @Operation(summary = "Atualizar nota de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoAvaliacaoDTO>> atualizar(@PathVariable Long id,
                                                                    @RequestBody AlunoAvaliacaoDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(
                new AlunoAvaliacaoDTO(service.atualizar(id, dto.getNota(), dto.getObs()))));
    }

    /**
     * Desativa (soft delete) um registro de nota.
     *
     * <p>O registro não é excluído fisicamente — permanece no banco com
     * {@code ativo = false} para preservar o histórico pedagógico.
     *
     * @param id ID do registro a desativar
     * @return confirmação com status HTTP 200
     */
    @Operation(summary = "Desativar nota (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Nota desativada com sucesso"));
    }
}
