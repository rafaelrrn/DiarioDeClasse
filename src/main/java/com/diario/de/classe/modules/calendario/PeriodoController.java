package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.calendario.dto.PeriodoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/periodos")
@Tag(name = "Periodo", description = "Gerenciamento de períodos")
public class PeriodoController {

    private final PeriodoService service;

    public PeriodoController(PeriodoService service) { this.service = service; }

    @Operation(summary = "Listar todos os períodos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<PeriodoDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(PeriodoDTO::new).toList());
    }

    @Operation(summary = "Buscar período por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PeriodoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new PeriodoDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar período")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<PeriodoDTO> criar(@RequestBody PeriodoDTO dto) {
        Periodo entity = new Periodo();
        BeanUtils.copyProperties(dto, entity, "idPeriodo");
        return ResponseEntity.status(HttpStatus.CREATED).body(new PeriodoDTO(service.criar(entity)));
    }

    @Operation(summary = "Atualizar período")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PeriodoDTO> atualizar(@PathVariable Long id, @RequestBody PeriodoDTO dto) {
        Periodo dados = new Periodo();
        BeanUtils.copyProperties(dto, dados, "idPeriodo");
        return ResponseEntity.ok(new PeriodoDTO(service.atualizar(id, dados)));
    }

    @Operation(summary = "Excluir período")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
