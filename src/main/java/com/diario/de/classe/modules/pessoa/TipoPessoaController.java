package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.TipoPessoaDTO;
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
    public ResponseEntity<List<TipoPessoaDTO>> listar() {
        List<TipoPessoaDTO> dtos = service.buscarTodos().stream().map(TipoPessoaDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar tipo de pessoa por ID")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoPessoaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new TipoPessoaDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar novo tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<TipoPessoaDTO> criar(@RequestBody TipoPessoaDTO dto) {
        TipoPessoa entity = new TipoPessoa();
        BeanUtils.copyProperties(dto, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TipoPessoaDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoPessoaDTO> atualizar(@PathVariable Long id, @RequestBody TipoPessoaDTO dto) {
        TipoPessoa dados = new TipoPessoa();
        BeanUtils.copyProperties(dto, dados);
        return ResponseEntity.ok(new TipoPessoaDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir tipo de pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
