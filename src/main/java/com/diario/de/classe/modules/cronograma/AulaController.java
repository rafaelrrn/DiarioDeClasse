package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.AulaDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/aulas")
@Tag(name = "Aula", description = "Registro de aulas — ocorrência concreta de aula vinculada a um período letivo")
public class AulaController {

    private final AulaService service;

    public AulaController(AulaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as aulas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AulaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar aula por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AulaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar aulas de uma classe em uma data específica")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/classe/{idClasse}/data/{data}")
    public ResponseEntity<ApiResponse<List<AulaDTO>>> buscarPorClasseEData(
            @PathVariable Long idClasse,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorClasseEData(idClasse, data)));
    }

    @Operation(summary = "Listar aulas de uma classe em um período letivo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/periodo/{idPeriodo}/classe/{idClasse}")
    public ResponseEntity<ApiResponse<List<AulaDTO>>> buscarPorPeriodoEClasse(
            @PathVariable Long idPeriodo,
            @PathVariable Long idClasse) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorPeriodoEClasse(idPeriodo, idClasse)));
    }

    @Operation(
            summary = "Registrar nova aula",
            description = """
                    Registra uma aula para a classe e período letivo informados.
                    A data deve estar dentro do intervalo do período letivo.
                    Não é permitido registrar duas aulas com o mesmo número na mesma data e classe.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Aula registrada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Classe, período letivo ou professor não encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Aula duplicada ou data fora do período letivo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Perfil sem permissão (requer ADMINISTRADOR ou PROFESSOR)")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AulaDTO>> criar(@RequestBody AulaDTO dto) {
        Aula entity = new Aula();
        entity.setDataAula(dto.getDataAula());
        entity.setNumeroAula(dto.getNumeroAula());
        entity.setConteudoMinistrado(dto.getConteudoMinistrado());
        entity.setChamadaEncerrada(dto.getChamadaEncerrada() != null ? dto.getChamadaEncerrada() : false);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        service.criar(entity, dto.getIdClasse(), dto.getIdPeriodoLetivo(), dto.getIdRegistradoPor()),
                        "Aula registrada com sucesso"));
    }

    @Operation(summary = "Atualizar dados da aula",
               description = "Não é possível alterar uma aula com chamada já encerrada.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AulaDTO>> atualizar(
            @PathVariable Long id, @RequestBody AulaDTO dto) {
        Aula dados = new Aula();
        dados.setDataAula(dto.getDataAula());
        dados.setNumeroAula(dto.getNumeroAula());
        dados.setConteudoMinistrado(dto.getConteudoMinistrado());
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, dados)));
    }

    @Operation(summary = "Encerrar chamada da aula",
               description = "Após encerrar, a chamada não pode mais ser alterada.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Chamada encerrada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Chamada já encerrada anteriormente")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESSOR')")
    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<ApiResponse<AulaDTO>> encerrarChamada(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.encerrarChamada(id), "Chamada encerrada com sucesso"));
    }

    @Operation(summary = "Desativar aula (soft delete)",
               description = "Não é possível excluir uma aula com chamada já encerrada.")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Aula desativada com sucesso"));
    }
}
