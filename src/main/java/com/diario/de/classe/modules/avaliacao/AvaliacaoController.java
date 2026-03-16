package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.modules.avaliacao.dto.AvaliacaoDTO;
import com.diario.de.classe.modules.avaliacao.dto.NotaLancamentoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/avaliacoes")
@Tag(name = "Avaliacao", description = "Gerenciamento de avaliações e lançamento de notas em lote")
public class AvaliacaoController {

    private final AvaliacaoService service;
    private final AlunoAvaliacaoService alunoAvaliacaoService;

    public AvaliacaoController(AvaliacaoService service, AlunoAvaliacaoService alunoAvaliacaoService) {
        this.service = service;
        this.alunoAvaliacaoService = alunoAvaliacaoService;
    }

    /**
     * Lista todas as avaliações ativas.
     *
     * @return lista de avaliações com status HTTP 200
     */
    @Operation(summary = "Listar todas as avaliações")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AvaliacaoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AvaliacaoDTO::new).toList()));
    }

    /**
     * Busca uma avaliação pelo ID.
     *
     * @param id ID da avaliação
     * @return avaliação encontrada
     */
    @Operation(summary = "Buscar avaliação por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AvaliacaoDTO(service.buscarPorId(id))));
    }

    /**
     * Cria uma nova avaliação.
     *
     * <p>O campo {@code peso} define a relevância desta avaliação no cálculo
     * da média ponderada. Exemplo: prova = 7, trabalho = 3.
     *
     * @param dto dados da avaliação (disciplina, materia, dia, peso)
     * @return avaliação criada com status HTTP 201
     */
    @Operation(summary = "Criar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> criar(@RequestBody AvaliacaoDTO dto) {
        Avaliacao entity = new Avaliacao();
        entity.setMateria(dto.getMateria());
        entity.setDia(dto.getDia());
        entity.setPeso(dto.getPeso());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        new AvaliacaoDTO(service.criar(entity, dto.getIdDisciplina(), dto.getIdCalendarioEscolar())),
                        "Avaliação criada com sucesso"));
    }

    /**
     * Atualiza uma avaliação existente.
     *
     * @param id  ID da avaliação a atualizar
     * @param dto novos dados da avaliação
     * @return avaliação atualizada
     */
    @Operation(summary = "Atualizar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> atualizar(@PathVariable Long id,
                                                               @RequestBody AvaliacaoDTO dto) {
        Avaliacao dados = new Avaliacao();
        dados.setMateria(dto.getMateria());
        dados.setDia(dto.getDia());
        dados.setPeso(dto.getPeso());
        return ResponseEntity.ok(ApiResponse.ok(
                new AvaliacaoDTO(service.atualizar(id, dados, dto.getIdDisciplina(), dto.getIdCalendarioEscolar()))));
    }

    /**
     * Desativa (soft delete) uma avaliação.
     *
     * <p>O registro não é excluído fisicamente — permanece no banco com
     * {@code ativo = false} para preservar o histórico de notas vinculadas.
     *
     * @param id ID da avaliação a desativar
     * @return confirmação com status HTTP 200
     */
    @Operation(summary = "Desativar avaliação (soft delete)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Avaliação desativada com sucesso"));
    }

    /**
     * Lança notas em lote para todos os alunos de uma avaliação.
     *
     * <p>Recebe uma lista de pares (idAluno, nota) e persiste cada uma.
     * Toda a operação é transacional: se um lançamento falhar, nenhum é salvo.
     * Caso um aluno já tenha nota ativa nesta avaliação, a requisição é rejeitada
     * com HTTP 422.
     *
     * @param id    ID da avaliação alvo (path variable)
     * @param notas lista de {@link NotaLancamentoDTO} com idAluno, nota e obs
     * @return lista de notas criadas com status HTTP 201
     */
    @Operation(
            summary = "Lançar notas em lote",
            description = """
                    Registra a nota de múltiplos alunos para uma mesma avaliação em uma única requisição.
                    A operação é transacional: qualquer erro cancela todos os lançamentos.
                    Retorna HTTP 422 se algum aluno já tiver nota ativa nesta avaliação.
                    """
    )
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping("/{id}/notas")
    public ResponseEntity<ApiResponse<List<AlunoAvaliacaoDTO>>> lancarNotas(
            @PathVariable Long id,
            @Valid @RequestBody List<NotaLancamentoDTO> notas) {
        List<AlunoAvaliacaoDTO> resultado = alunoAvaliacaoService.lancarNotasEmLote(id, notas)
                .stream().map(AlunoAvaliacaoDTO::new).toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(resultado, "Notas lançadas com sucesso"));
    }
}
