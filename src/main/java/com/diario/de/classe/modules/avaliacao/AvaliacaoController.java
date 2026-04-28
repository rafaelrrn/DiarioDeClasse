package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.modules.avaliacao.dto.AvaliacaoDTO;
import com.diario.de.classe.modules.avaliacao.dto.NotaLancamentoDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Listar todas as avaliações")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AvaliacaoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AvaliacaoDTO::new).toList()));
    }

    @Operation(summary = "Buscar avaliação por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AvaliacaoDTO(service.buscarPorId(id))));
    }

    /**
     * Cria uma nova avaliação.
     *
     * <p>Campos opcionais: {@code idCalendarioEscolar} (legado), {@code idPeriodoLetivo} (novo),
     * {@code tipo} (padrão: PROVA), {@code dia}, {@code peso}.
     */
    @Operation(summary = "Criar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> criar(@RequestBody AvaliacaoDTO dto) {
        Avaliacao entity = new Avaliacao();
        entity.setMateria(dto.getMateria());
        entity.setDia(dto.getDia());
        entity.setPeso(dto.getPeso());
        entity.setTipo(dto.getTipo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        new AvaliacaoDTO(service.criar(
                                entity,
                                dto.getIdDisciplina(),
                                dto.getIdCalendarioEscolar(),
                                dto.getIdPeriodoLetivo())),
                        "Avaliação criada com sucesso"));
    }

    @Operation(summary = "Atualizar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AvaliacaoDTO>> atualizar(@PathVariable Long id,
                                                               @RequestBody AvaliacaoDTO dto) {
        Avaliacao dados = new Avaliacao();
        dados.setMateria(dto.getMateria());
        dados.setDia(dto.getDia());
        dados.setPeso(dto.getPeso());
        dados.setTipo(dto.getTipo());
        return ResponseEntity.ok(ApiResponse.ok(
                new AvaliacaoDTO(service.atualizar(
                        id,
                        dados,
                        dto.getIdDisciplina(),
                        dto.getIdCalendarioEscolar(),
                        dto.getIdPeriodoLetivo()))));
    }

    @Operation(summary = "Desativar avaliação (soft delete)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Avaliação desativada com sucesso"));
    }

    @Operation(
            summary = "Lançar notas em lote",
            description = """
                    Registra a nota de múltiplos alunos para uma mesma avaliação em uma única requisição.
                    A operação é transacional: qualquer erro cancela todos os lançamentos.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notas lançadas com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Avaliação ou aluno não encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Algum aluno já possui nota ativa nesta avaliação"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Perfil sem permissão (requer ADMINISTRADOR ou PROFESSOR)")
    })
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
