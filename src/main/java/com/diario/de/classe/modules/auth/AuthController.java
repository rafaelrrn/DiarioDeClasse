package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.auth.dto.UserMeResponse;
import com.diario.de.classe.shared.dto.ApiResponse;
import com.diario.de.classe.shared.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação: registro, login, logout e dados do usuário logado.
 *
 * <p>Rotas públicas (liberadas no SecurityConfig):
 * <ul>
 *   <li>{@code POST /v1/auth/register} — cria novo usuário</li>
 *   <li>{@code POST /v1/auth/login} — autentica e emite cookie JWT</li>
 *   <li>{@code POST /v1/auth/logout} — invalida o cookie</li>
 *   <li>{@code GET  /v1/auth/me} — dados do usuário autenticado</li>
 * </ul>
 */
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Autenticação", description = "Login, registro, logout e consulta do usuário autenticado")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registra um novo usuário, codificando a senha antes de persistir.
     *
     * <p>A entidade {@code User} não é exposta na resposta para evitar
     * vazamento de dados sensíveis.
     *
     * @param user dados do usuário (nome, email, senha, role)
     * @return confirmação de registro
     */
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema. A senha é armazenada com BCrypt. " +
                          "Não retorna o usuário criado para evitar exposição de dados sensíveis."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuário registrado com sucesso"));
    }

    /**
     * Autentica o usuário e emite um token JWT em cookie HttpOnly.
     *
     * <p>O cookie HttpOnly impede acesso via JavaScript, protegendo contra ataques XSS.
     * Duração do cookie: 1 dia (86.400 segundos).
     *
     * <p>Após o login bem-sucedido no Swagger UI, o browser armazena o cookie
     * automaticamente e todas as chamadas subsequentes serão autenticadas.
     *
     * @param user     credenciais de acesso (email + senha)
     * @param response resposta HTTP onde o cookie {@code auth_token} será definido
     * @return confirmação de login (o token fica no cookie, não no body)
     */
    @Operation(
            summary = "Fazer login",
            description = """
                    Autentica com email e senha. Em caso de sucesso, define o cookie **auth_token** (HttpOnly).

                    **Como usar no Swagger UI:**
                    1. Execute este endpoint com suas credenciais.
                    2. O browser armazenará o cookie automaticamente.
                    3. Os demais endpoints já estarão autenticados.

                    **Corpo esperado:**
                    ```json
                    { "email": "usuario@exemplo.com", "senha": "minhasenha" }
                    ```
                    """
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody User user, HttpServletResponse response) {
        var userFromDb = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(user.getSenha(), userFromDb.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.generateToken(userFromDb.getEmail(), userFromDb.getRole().name());

        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1 dia
        // cookie.setSecure(true); // ativar em produção com HTTPS

        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.ok(null, "Login realizado com sucesso"));
    }

    /**
     * Encerra a sessão do usuário zerando o cookie de autenticação.
     *
     * <p>Define o cookie {@code auth_token} com {@code maxAge=0}, fazendo o browser
     * descartá-lo imediatamente. O token não é invalidado no servidor (stateless JWT).
     */
    @Operation(
            summary = "Fazer logout",
            description = "Encerra a sessão zerando o cookie auth_token no browser. " +
                          "O token JWT não é invalidado no servidor (design stateless)."
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expira imediatamente

        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.ok(null, "Logout realizado com sucesso"));
    }

    /**
     * Retorna os dados do usuário atualmente autenticado, lidos a partir do token JWT.
     *
     * @param request requisição com o cookie auth_token
     * @return dados básicos do usuário: id, email, nome e role
     */
    @Operation(
            summary = "Dados do usuário autenticado",
            description = "Retorna id, email, nome e role do usuário identificado pelo cookie auth_token."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserMeResponse>> me(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new RuntimeException("Não autenticado");

        String token = null;
        for (Cookie cookie : cookies) {
            if ("auth_token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null || !jwtService.isTokenValid(token)) {
            throw new RuntimeException("Token inválido");
        }

        String email = jwtService.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(ApiResponse.ok(
                new UserMeResponse(user.getIdUsers(), user.getEmail(), user.getNome(), user.getRole())
        ));
    }
}
