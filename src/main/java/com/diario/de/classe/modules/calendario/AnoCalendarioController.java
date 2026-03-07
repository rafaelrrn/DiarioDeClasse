package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.calendario.dto.AnoCalendarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anos-calendario")
@Tag(name = "AnoCalendario", description = "Gerenciamento de anos do calendário")
public class AnoCalendarioController {

    private final AnoCalendarioService service;

    public AnoCalendarioController(AnoCalendarioService service) { this.service = service; }

    @Operation(summary = "Listar todos os anos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<AnoCalendarioDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(AnoCalendarioDTO::new).toList());
    }

    @Operation(summary = "Buscar ano por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AnoCalendarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AnoCalendarioDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar ano calendário")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<AnoCalendarioDTO> criar(@RequestBody AnoCalendarioDTO dto) {
        AnoCalendario entity = new AnoCalendario();
        BeanUtils.copyProperties(dto, entity, "idAnoCalendario");
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnoCalendarioDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar ano calendário")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AnoCalendarioDTO> atualizar(@PathVariable Long id, @RequestBody AnoCalendarioDTO dto) {
        AnoCalendario dados = new AnoCalendario();
        BeanUtils.copyProperties(dto, dados, "idAnoCalendario");
        return ResponseEntity.ok(new AnoCalendarioDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir ano calendário")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
