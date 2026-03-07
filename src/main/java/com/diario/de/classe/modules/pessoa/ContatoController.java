package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.modules.pessoa.dto.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/contatos")
@Tag(name = "Contato", description = "Gerenciamento de contatos")
public class ContatoController {

    private final ContatoService service;

    public ContatoController(ContatoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os contatos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<ContatoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(ContatoDTO::new).toList());
    }

    @Operation(summary = "Buscar contato por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ContatoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ContatoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar novo contato")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ContatoDTO> criar(@RequestBody ContatoDTO dto) {
        Contato entity = new Contato();
        BeanUtils.copyProperties(dto, entity, "idContato");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ContatoDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar contato")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ContatoDTO> atualizar(@PathVariable Long id, @RequestBody ContatoDTO dto) {
        Contato dados = new Contato();
        BeanUtils.copyProperties(dto, dados, "idContato");
        return ResponseEntity.ok(new ContatoDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir contato")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
