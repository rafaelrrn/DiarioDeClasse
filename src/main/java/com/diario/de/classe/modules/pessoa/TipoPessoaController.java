package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.TipoPessoaDTO;
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
@RequestMapping("/v1/tipos-pessoa")
@Tag(name = "TipoPessoa", description = "Gerenciamento de tipos de pessoa")
public class TipoPessoaController {

    private final TipoPessoaService service;

    public TipoPessoaController(TipoPessoaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os tipos de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoPessoaDTO>>> listar() {
        List<TipoPessoaDTO> dtos = service.buscarTodos().stream().map(TipoPessoaDTO::new).toList();
        return ResponseEntity.ok(ApiResponse.ok(dtos));
    }

    @Operation(summary = "Buscar tipo de pessoa por ID")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoPessoaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new TipoPessoaDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar novo tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<TipoPessoaDTO>> criar(@RequestBody TipoPessoaDTO dto) {
        TipoPessoa entity = new TipoPessoa();
        BeanUtils.copyProperties(dto, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new TipoPessoaDTO(service.criar(entity)), "Tipo de pessoa criado com sucesso"));
    }

    @Operation(summary = "Atualizar tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoPessoaDTO>> atualizar(@PathVariable Long id, @RequestBody TipoPessoaDTO dto) {
        TipoPessoa dados = new TipoPessoa();
        BeanUtils.copyProperties(dto, dados);
        return ResponseEntity.ok(ApiResponse.ok(new TipoPessoaDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
