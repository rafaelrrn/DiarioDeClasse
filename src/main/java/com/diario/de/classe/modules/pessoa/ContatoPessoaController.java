package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.ContatoPessoaDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/contatos-pessoa")
@Tag(name = "ContatoPessoa", description = "Associação entre Pessoa e Contato")
public class ContatoPessoaController {

    private final ContatoPessoaService service;

    public ContatoPessoaController(ContatoPessoaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as associações contato-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ContatoPessoaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(ContatoPessoaDTO::new).toList()));
    }

    @Operation(summary = "Buscar associação por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContatoPessoaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new ContatoPessoaDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar nova associação contato-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ContatoPessoaDTO>> criar(@RequestBody ContatoPessoaDTO dto) {
        ContatoPessoa entity = new ContatoPessoa();
        BeanUtils.copyProperties(dto, entity, "idContatoPessoa", "idPessoa", "idContato");
        entity.setNome(dto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new ContatoPessoaDTO(service.criar(entity, dto.getIdPessoa(), dto.getIdContato())), "Contato da pessoa criado com sucesso"));
    }

    @Operation(summary = "Atualizar associação contato-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContatoPessoaDTO>> atualizar(@PathVariable Long id, @RequestBody ContatoPessoaDTO dto) {
        ContatoPessoa dados = new ContatoPessoa();
        dados.setNome(dto.getNome());
        return ResponseEntity.ok(ApiResponse.ok(new ContatoPessoaDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir associação contato-pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
