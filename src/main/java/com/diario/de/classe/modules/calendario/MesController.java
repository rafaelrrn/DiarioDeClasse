package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.calendario.dto.MesDTO;
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
@RequestMapping("/v1/meses")
@Tag(name = "Mes", description = "Gerenciamento de meses")
public class MesController {

    private final MesService service;

    public MesController(MesService service) { this.service = service; }

    @Operation(summary = "Listar todos os meses")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MesDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(MesDTO::new).toList()));
    }

    @Operation(summary = "Buscar mês por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MesDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new MesDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar mês")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<MesDTO>> criar(@RequestBody MesDTO dto) {
        Mes entity = new Mes();
        BeanUtils.copyProperties(dto, entity, "idMes");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(new MesDTO(service.criar(entity)), "Mês criado com sucesso"));
    }

    @Operation(summary = "Atualizar mês")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MesDTO>> atualizar(@PathVariable Long id, @RequestBody MesDTO dto) {
        Mes dados = new Mes();
        BeanUtils.copyProperties(dto, dados, "idMes");
        return ResponseEntity.ok(ApiResponse.ok(new MesDTO(service.atualizar(id, dados))));
    }

    @Operation(summary = "Excluir mês")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
