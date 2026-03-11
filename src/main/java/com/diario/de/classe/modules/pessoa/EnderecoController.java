package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.EnderecoDTO;
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
@RequestMapping("/v1/enderecos")
@Tag(name = "Endereco", description = "Gerenciamento de endereços")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os endereços")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnderecoDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(EnderecoDTO::new).toList()));
    }

    @Operation(summary = "Buscar endereço por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnderecoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new EnderecoDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar novo endereço")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<EnderecoDTO>> criar(@RequestBody EnderecoDTO dto) {
        Endereco entity = new Endereco();
        BeanUtils.copyProperties(dto, entity, "idEndereco");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new EnderecoDTO(service.criar(entity)), "Endereço criado com sucesso"));
    }

    @Operation(summary = "Atualizar endereço")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnderecoDTO>> atualizar(@PathVariable Long id, @RequestBody EnderecoDTO dto) {
        Endereco dados = new Endereco();
        BeanUtils.copyProperties(dto, dados, "idEndereco");
        return ResponseEntity.ok(ApiResponse.ok(new EnderecoDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir endereço")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
