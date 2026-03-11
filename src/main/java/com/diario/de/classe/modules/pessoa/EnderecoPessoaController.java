package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.EnderecoPessoaDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enderecos-pessoa")
@Tag(name = "EnderecoPessoa", description = "Associação entre Pessoa e Endereço")
public class EnderecoPessoaController {

    private final EnderecoPessoaService service;

    public EnderecoPessoaController(EnderecoPessoaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as associações endereço-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnderecoPessoaDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(EnderecoPessoaDTO::new).toList()));
    }

    @Operation(summary = "Buscar associação por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnderecoPessoaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new EnderecoPessoaDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar nova associação endereço-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<EnderecoPessoaDTO>> criar(@RequestBody EnderecoPessoaDTO dto) {
        EnderecoPessoa entity = new EnderecoPessoa();
        entity.setNome(dto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new EnderecoPessoaDTO(service.criar(entity, dto.getIdPessoa(), dto.getIdEndereco())), "Endereço da pessoa criado com sucesso"));
    }

    @Operation(summary = "Atualizar associação endereço-pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnderecoPessoaDTO>> atualizar(@PathVariable Long id, @RequestBody EnderecoPessoaDTO dto) {
        EnderecoPessoa dados = new EnderecoPessoa();
        dados.setNome(dto.getNome());
        return ResponseEntity.ok(ApiResponse.ok(new EnderecoPessoaDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir associação endereço-pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
