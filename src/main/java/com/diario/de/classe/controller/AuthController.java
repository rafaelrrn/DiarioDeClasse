package com.diario.de.classe.controller;

import com.diario.de.classe.model.User;
import com.diario.de.classe.repository.jpa.UserRepository;
import com.diario.de.classe.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return repository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        var userFromDb = repository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(user.getSenha(), userFromDb.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        return jwtUtil.generateToken(userFromDb.getEmail(), userFromDb.getRole().name());
    }


}
