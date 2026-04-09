package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.AlunoPerfilDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/aluno-perfil")
@Tag(name = "AlunoPerfil", description = "Perfil específico de aluno — matrícula e necessidades educacionais especiais")
public class AlunoPerfilController {

    private final AlunoPerfilService service;

    public AlunoPerfilController(AlunoPerfilService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os perfis de aluno")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlunoPerfilDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarTodos().stream().map(AlunoPerfilDTO::new).toList()));
    }

    @Operation(summary = "Buscar perfil de aluno por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoPerfilDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoPerfilDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Buscar perfil de aluno pela Pessoa",
               description = "Retorna o perfil de aluno vinculado à Pessoa informada.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/pessoa/{idPessoa}")
    public ResponseEntity<ApiResponse<AlunoPerfilDTO>> buscarPorPessoa(@PathVariable Long idPessoa) {
        return ResponseEntity.ok(ApiResponse.ok(new AlunoPerfilDTO(service.buscarPorPessoa(idPessoa))));
    }

    @Operation(summary = "Criar perfil de aluno",
               description = "Cria o perfil de aluno para a Pessoa informada em idPessoa. "
                           + "Cada Pessoa só pode ter um perfil de aluno.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AlunoPerfilDTO>> criar(@RequestBody AlunoPerfilDTO dto) {
        AlunoPerfil entity = new AlunoPerfil();
        entity.setMatricula(dto.getMatricula());
        entity.setDataMatricula(dto.getDataMatricula());
        entity.setNecessidadeEspecial(dto.getNecessidadeEspecial() != null ? dto.getNecessidadeEspecial() : false);
        entity.setDescricaoNee(dto.getDescricaoNee());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new AlunoPerfilDTO(service.criar(dto.getIdPessoa(), entity)),
                        "Perfil de aluno criado com sucesso"));
    }

    @Operation(summary = "Atualizar perfil de aluno",
               description = "Atualiza matrícula, data de matrícula e dados de NEE. "
                           + "O vínculo com Pessoa não pode ser alterado.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoPerfilDTO>> atualizar(
            @PathVariable Long id, @RequestBody AlunoPerfilDTO dto) {
        AlunoPerfil dados = new AlunoPerfil();
        dados.setMatricula(dto.getMatricula());
        dados.setDataMatricula(dto.getDataMatricula());
        dados.setNecessidadeEspecial(dto.getNecessidadeEspecial() != null ? dto.getNecessidadeEspecial() : false);
        dados.setDescricaoNee(dto.getDescricaoNee());
        return ResponseEntity.ok(ApiResponse.ok(new AlunoPerfilDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir perfil de aluno")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Perfil de aluno excluído com sucesso"));
    }
}
