package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.auth.dto.UserMeResponse;
import com.diario.de.classe.shared.dto.ApiResponse;
import com.diario.de.classe.shared.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação: registro, login, logout e dados do usuário logado.
 *
 * Rotas públicas (liberadas no SecurityConfig):
 *   POST /v1/auth/register
 *   POST /v1/auth/login
 *   POST /v1/auth/logout
 *   GET  /v1/auth/me
 */
@RestController
@RequestMapping("/v1/auth")
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
     * A entidade User não é exposta diretamente — retorna apenas confirmação.
     *
     * @param user Dados do usuário (nome, email, senha, role)
     * @return Confirmação de registro sem expor dados sensíveis
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuário registrado com sucesso"));
    }

    /**
     * Autentica o usuário e emite um token JWT em cookie HttpOnly.
     *
     * O cookie HttpOnly impede acesso via JavaScript, protegendo contra ataques XSS.
     * Duração do cookie: 1 dia.
     *
     * @param user     Credenciais (email + senha)
     * @param response Resposta HTTP onde o cookie será adicionado
     */
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
     * Encerra a sessão do usuário limpando o cookie de autenticação.
     */
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
     * @param request Requisição com o cookie auth_token
     * @return Dados básicos do usuário (id, email, nome, role)
     */
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
