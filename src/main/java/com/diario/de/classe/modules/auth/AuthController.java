package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.auth.dto.UserMeResponse;
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
 *   POST /auth/register
 *   POST /auth/login
 *   POST /auth/logout
 *   GET  /auth/me
 */
@RestController
@RequestMapping("/auth")
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
     * @param user Dados do usuário (nome, email, senha, role)
     * @return Usuário persistido (sem a senha em texto claro — TODO: usar DTO de resposta)
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return userRepository.save(user);
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
    public ResponseEntity<Void> login(@RequestBody User user, HttpServletResponse response) {
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

        return ResponseEntity.ok().build();
    }

    /**
     * Encerra a sessão do usuário limpando o cookie de autenticação.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expira imediatamente

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    /**
     * Retorna os dados do usuário atualmente autenticado, lidos a partir do token JWT.
     *
     * @param request Requisição com o cookie auth_token
     * @return Dados básicos do usuário (id, email, nome, role)
     */
    @GetMapping("/me")
    public UserMeResponse me(HttpServletRequest request) {
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

        return new UserMeResponse(user.getIdUsers(), user.getEmail(), user.getNome(), user.getRole());
    }
}
