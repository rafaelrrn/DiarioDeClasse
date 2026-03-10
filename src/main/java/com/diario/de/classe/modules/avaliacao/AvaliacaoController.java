package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.AvaliacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/avaliacoes")
@Tag(name = "Avaliacao", description = "Gerenciamento de avaliações")
public class AvaliacaoController {

    private final AvaliacaoService service;

    public AvaliacaoController(AvaliacaoService service) { this.service = service; }

    @Operation(summary = "Listar todas as avaliações")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(AvaliacaoDTO::new).toList());
    }

    @Operation(summary = "Buscar avaliação por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AvaliacaoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criar(@RequestBody AvaliacaoDTO dto) {
        Avaliacao entity = new Avaliacao();
        entity.setMateria(dto.getMateria());
        entity.setDia(dto.getDia());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AvaliacaoDTO(service.criar(entity, dto.getIdDisciplina(), dto.getIdCalendarioEscolar())));
    }

    @Operation(summary = "Atualizar avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> atualizar(@PathVariable Long id, @RequestBody AvaliacaoDTO dto) {
        Avaliacao dados = new Avaliacao();
        dados.setMateria(dto.getMateria());
        dados.setDia(dto.getDia());
        return ResponseEntity.ok(new AvaliacaoDTO(service.atualizar(id, dados, dto.getIdDisciplina(), dto.getIdCalendarioEscolar())));
    }

    @Operation(summary = "Excluir avaliação")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
