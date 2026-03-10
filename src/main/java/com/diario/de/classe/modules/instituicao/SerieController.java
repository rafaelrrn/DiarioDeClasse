package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.modules.instituicao.dto.SerieDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/series")
@Tag(name = "Serie", description = "Gerenciamento de Serie")
public class SerieController {

    private final SerieService service;
    public SerieController(SerieService service) { this.service = service; }

    @Operation(summary = "Listar todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<SerieDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(SerieDTO::new).toList());
    }

    @Operation(summary = "Buscar por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new SerieDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<SerieDTO> criar(@RequestBody SerieDTO dto) {
        Serie entity = new Serie();
        BeanUtils.copyProperties(dto, entity, "idSerie");
        return ResponseEntity.status(HttpStatus.CREATED).body(new SerieDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<SerieDTO> atualizar(@PathVariable Long id, @RequestBody SerieDTO dto) {
        Serie dados = new Serie();
        BeanUtils.copyProperties(dto, dados, "idSerie");
        return ResponseEntity.ok(new SerieDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
