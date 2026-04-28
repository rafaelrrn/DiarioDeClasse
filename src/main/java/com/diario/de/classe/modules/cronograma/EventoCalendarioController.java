package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.EventoCalendarioDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/eventos-calendario")
@Tag(name = "EventoCalendario", description = "Gerenciamento de eventos do calendário escolar (feriados, recessos, eventos)")
public class EventoCalendarioController {

    private final EventoCalendarioService service;

    public EventoCalendarioController(EventoCalendarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os eventos de calendário")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoCalendarioDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos()));
    }

    @Operation(summary = "Buscar evento por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoCalendarioDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar eventos de um cronograma, ordenados por data de início")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
    @GetMapping("/cronograma/{id}")
    public ResponseEntity<ApiResponse<List<EventoCalendarioDTO>>> buscarPorCronograma(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorCronograma(id)));
    }

    @Operation(summary = "Criar evento de calendário")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<EventoCalendarioDTO>> criar(@RequestBody EventoCalendarioDTO dto) {
        EventoCalendario entity = new EventoCalendario();
        entity.setTitulo(dto.getTitulo());
        entity.setDescricao(dto.getDescricao());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setTipoEvento(dto.getTipoEvento());
        entity.setEhLetivo(dto.getEhLetivo() != null ? dto.getEhLetivo() : false);
        entity.setCor(dto.getCor());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.criar(entity, dto.getIdCronogramaAnual()),
                        "Evento de calendário criado com sucesso"));
    }

    @Operation(summary = "Atualizar evento de calendário")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoCalendarioDTO>> atualizar(
            @PathVariable Long id, @RequestBody EventoCalendarioDTO dto) {
        EventoCalendario dados = new EventoCalendario();
        dados.setTitulo(dto.getTitulo());
        dados.setDescricao(dto.getDescricao());
        dados.setDataInicio(dto.getDataInicio());
        dados.setDataFim(dto.getDataFim());
        dados.setTipoEvento(dto.getTipoEvento());
        dados.setEhLetivo(dto.getEhLetivo() != null ? dto.getEhLetivo() : false);
        dados.setCor(dto.getCor());
        return ResponseEntity.ok(ApiResponse.ok(service.atualizar(id, dados)));
    }

    @Operation(summary = "Desativar evento de calendário (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Evento de calendário desativado com sucesso"));
    }
}
