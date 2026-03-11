package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.PessoaResponsavelDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pessoas-responsavel")
@Tag(name = "PessoaResponsavel", description = "Vinculação de alunos com seus responsáveis legais")
public class PessoaResponsavelController {

    private final PessoaResponsavelService service;

    public PessoaResponsavelController(PessoaResponsavelService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os vínculos aluno-responsável")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PessoaResponsavelDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(PessoaResponsavelDTO::new).toList()));
    }

    @Operation(summary = "Buscar vínculo por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoaResponsavelDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new PessoaResponsavelDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar vínculo aluno-responsável")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<PessoaResponsavelDTO>> criar(@RequestBody PessoaResponsavelDTO dto) {
        PessoaResponsavel entity = new PessoaResponsavel();
        entity.setParentesco(dto.getParentesco());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new PessoaResponsavelDTO(service.criar(entity, dto.getIdAluno(), dto.getIdResponsavel())), "Vínculo com responsável criado com sucesso"));
    }

    @Operation(summary = "Atualizar vínculo aluno-responsável")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoaResponsavelDTO>> atualizar(@PathVariable Long id, @RequestBody PessoaResponsavelDTO dto) {
        PessoaResponsavel dados = new PessoaResponsavel();
        dados.setParentesco(dto.getParentesco());
        return ResponseEntity.ok(ApiResponse.ok(new PessoaResponsavelDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir vínculo aluno-responsável")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
