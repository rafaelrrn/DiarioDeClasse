package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.auth.dto.UserMeResponse;
import com.diario.de.classe.modules.auth.dto.UserUpdateRequest;
import com.diario.de.classe.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gerenciamento de usuários do sistema.
 *
 * <p>Registro de novos usuários fica em {@code POST /v1/auth/register}.
 * Este controller cobre listagem, busca e atualização (incluindo vínculo com Pessoa).
 */
@RestController
@RequestMapping("/v1/users")
@Tag(name = "Usuários", description = "Listagem, busca e atualização de usuários (vínculo com Pessoa)")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos os usuários")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserMeResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(userService.buscarTodos()));
    }

    @Operation(summary = "Buscar usuário por ID")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserMeResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.buscarPorId(id)));
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza nome, email, role e vínculo com Pessoa. " +
                          "Para vincular, informe idPessoa. Para desvincular, envie idPessoa: null. " +
                          "Se senha for omitida ou vazia, a senha atual é mantida."
    )
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserMeResponse>> atualizar(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.atualizar(id, request), "Usuário atualizado com sucesso"));
    }
}
