package com.diario.de.classe.config;

import com.diario.de.classe.shared.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity ativa o processamento de @PreAuthorize em cada método dos controllers.
// Sem esta anotação, as regras @PreAuthorize são ignoradas silenciosamente pelo Spring.
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Hierarquia de roles: cada nível herda as permissões de todos os níveis abaixo.
     *
     * Exemplo: um ADMINISTRADOR pode acessar qualquer endpoint marcado com
     * @PreAuthorize("hasRole('PROFESSOR')") sem precisar listar todos os roles.
     *
     * Hierarquia completa: ADMINISTRADOR > DIRETOR > COORDENADOR > PROFESSOR > RESPONSAVEL > ALUNO
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("""
            ROLE_ADMINISTRADOR > ROLE_DIRETOR
            ROLE_DIRETOR > ROLE_COORDENADOR
            ROLE_COORDENADOR > ROLE_PROFESSOR
            ROLE_PROFESSOR > ROLE_RESPONSAVEL
            ROLE_RESPONSAVEL > ROLE_ALUNO
        """);
        return roleHierarchy;
    }

    /**
     * Cadeia de filtros de segurança.
     *
     * Estratégia: filterChain trata apenas autenticação (quem pode entrar).
     * A autorização por role (o que pode fazer) fica nos controllers via @PreAuthorize,
     * tornando a segurança visível e auditável diretamente no código de cada endpoint.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS — deve sempre ser permitido
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Rotas de autenticação — públicas (login, registro, logout)
                        .requestMatchers("/v1/auth/**").permitAll()
                        // Swagger UI / OpenAPI — públicos em dev
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Actuator — público em dev (health, info)
                        .requestMatchers("/actuator/**").permitAll()
                        // Todas as demais rotas exigem autenticação.
                        // A autorização por role é definida via @PreAuthorize em cada controller.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
