package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.calendario.dto.CalendarioEscolarDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/calendarios-escolares")
@Tag(name = "CalendarioEscolar", description = "Gerenciamento do calendário escolar")
public class CalendarioEscolarController {

    private final CalendarioEscolarService service;

    public CalendarioEscolarController(CalendarioEscolarService service) { this.service = service; }

    @Operation(summary = "Listar todos os calendários")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<CalendarioEscolarDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos().stream().map(CalendarioEscolarDTO::new).toList());
    }

    @Operation(summary = "Buscar calendário por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<CalendarioEscolarDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new CalendarioEscolarDTO(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar calendário escolar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<CalendarioEscolarDTO> criar(@RequestBody CalendarioEscolarDTO dto) {
        CalendarioEscolar entity = new CalendarioEscolar();
        BeanUtils.copyProperties(dto, entity, "idCalendarioEscolar", "mes", "anoCalendario", "periodo", "classe");
        entity.setDiasLetivos(dto.getDiasLetivos());
        entity.setDiasAvaliacoes(dto.getDiasAvaliacoes());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CalendarioEscolarDTO(service.criar(entity, dto.getIdMes(), dto.getIdAnoCalendario(),
                        dto.getIdPeriodo(), dto.getIdClasse())));
    }

    @Operation(summary = "Atualizar calendário escolar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CalendarioEscolarDTO> atualizar(@PathVariable Long id, @RequestBody CalendarioEscolarDTO dto) {
        CalendarioEscolar dados = new CalendarioEscolar();
        dados.setDiasLetivos(dto.getDiasLetivos());
        dados.setDiasAvaliacoes(dto.getDiasAvaliacoes());
        return ResponseEntity.ok(new CalendarioEscolarDTO(service.atualizar(id, dados, dto.getIdMes(),
                dto.getIdAnoCalendario(), dto.getIdPeriodo(), dto.getIdClasse())));
    }

    @Operation(summary = "Excluir calendário escolar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
