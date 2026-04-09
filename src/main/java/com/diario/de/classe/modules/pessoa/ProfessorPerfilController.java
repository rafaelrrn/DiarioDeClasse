package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.ProfessorPerfilDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/professor-perfil")
@Tag(name = "ProfessorPerfil", description = "Perfil específico de professor — registro MEC, formação e admissão")
public class ProfessorPerfilController {

    private final ProfessorPerfilService service;

    public ProfessorPerfilController(ProfessorPerfilService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os perfis de professor")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfessorPerfilDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(ProfessorPerfilDTO::new).toList()));
    }

    @Operation(summary = "Buscar perfil de professor por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessorPerfilDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new ProfessorPerfilDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Buscar perfil de professor pela Pessoa",
               description = "Retorna o perfil de professor vinculado à Pessoa informada.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/pessoa/{idPessoa}")
    public ResponseEntity<ApiResponse<ProfessorPerfilDTO>> buscarPorPessoa(@PathVariable Long idPessoa) {
        return ResponseEntity.ok(ApiResponse.ok(new ProfessorPerfilDTO(service.buscarPorPessoa(idPessoa))));
    }

    @Operation(summary = "Criar perfil de professor",
               description = "Cria o perfil de professor para a Pessoa informada em idPessoa. "
                           + "Cada Pessoa só pode ter um perfil de professor.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProfessorPerfilDTO>> criar(@RequestBody ProfessorPerfilDTO dto) {
        ProfessorPerfil entity = new ProfessorPerfil();
        entity.setRegistroMec(dto.getRegistroMec());
        entity.setFormacao(dto.getFormacao());
        entity.setDataAdmissao(dto.getDataAdmissao());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new ProfessorPerfilDTO(service.criar(dto.getIdPessoa(), entity)),
                        "Perfil de professor criado com sucesso"));
    }

    @Operation(summary = "Atualizar perfil de professor",
               description = "Atualiza registro MEC, formação e data de admissão. "
                           + "O vínculo com Pessoa não pode ser alterado.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessorPerfilDTO>> atualizar(
            @PathVariable Long id, @RequestBody ProfessorPerfilDTO dto) {
        ProfessorPerfil dados = new ProfessorPerfil();
        dados.setRegistroMec(dto.getRegistroMec());
        dados.setFormacao(dto.getFormacao());
        dados.setDataAdmissao(dto.getDataAdmissao());
        return ResponseEntity.ok(ApiResponse.ok(new ProfessorPerfilDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir perfil de professor")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Perfil de professor excluído com sucesso"));
    }
}
