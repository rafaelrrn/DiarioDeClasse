package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.PessoaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pessoas")
@Tag(name = "Pessoa", description = "Gerenciamento de pessoas (alunos, professores, responsáveis)")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as pessoas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'DIRETOR')")
    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listar() {
        List<PessoaDTO> dtos = service.buscarTodos().stream().map(PessoaDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar pessoa por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new PessoaDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar nova pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@RequestBody PessoaDTO dto) {
        Pessoa entity = new Pessoa();
        BeanUtils.copyProperties(dto, entity, "idPessoa", "idTipoPessoa");
        return ResponseEntity.status(HttpStatus.CREATED).body(new PessoaDTO(service.criar(entity, dto.getIdTipoPessoa())));
    }

    @Operation(summary = "Atualizar pessoa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @RequestBody PessoaDTO dto) {
        Pessoa dados = new Pessoa();
        BeanUtils.copyProperties(dto, dados, "idPessoa", "idTipoPessoa");
        return ResponseEntity.ok(new PessoaDTO(service.atualizar(id, dados, dto.getIdTipoPessoa())));
    }

    @Operation(summary = "Excluir pessoa")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
