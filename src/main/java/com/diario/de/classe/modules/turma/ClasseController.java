package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.turma.dto.ClasseDTO;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/classes")
@Tag(name = "Classe", description = "Gerenciamento de classes")
public class ClasseController {

    private final ClasseService service;

    public ClasseController(ClasseService service) { this.service = service; }

    @Operation(summary = "Listar todas as classes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClasseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarTodos().stream().map(ClasseDTO::new).toList()));
    }

    @Operation(summary = "Buscar classe por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClasseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(new ClasseDTO(service.buscarPorId(id))));
    }

    @Operation(summary = "Criar nova classe")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClasseDTO>> criar(@RequestBody ClasseDTO dto) {
        Classe entity = new Classe();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(new ClasseDTO(service.criar(entity, dto.getIdInstituicaoEnsino(),
                        dto.getIdComponenteCurricular(), dto.getIdCurso(), dto.getIdTurno(),
                        dto.getIdTurma(), dto.getIdProfessor())), "Classe criada com sucesso"));
    }

    @Operation(summary = "Atualizar classe")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClasseDTO>> atualizar(@PathVariable Long id, @RequestBody ClasseDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(new ClasseDTO(service.atualizar(id, dto.getIdInstituicaoEnsino(),
                dto.getIdComponenteCurricular(), dto.getIdCurso(), dto.getIdTurno(),
                dto.getIdTurma(), dto.getIdProfessor()))));
    }

    @Operation(summary = "Excluir classe")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Excluído com sucesso"));
    }
}
