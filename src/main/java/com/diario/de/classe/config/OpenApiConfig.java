package com.diario.de.classe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do SpringDoc/OpenAPI para o projeto DiárioDigital.
 *
 * <p>Gera documentação interativa acessível em:
 * <ul>
 *   <li>Swagger UI: {@code /swagger-ui.html}</li>
 *   <li>JSON OpenAPI: {@code /v3/api-docs}</li>
 * </ul>
 *
 * <p><strong>Autenticação no Swagger UI:</strong>
 * A API usa cookie HttpOnly ({@code auth_token}).
 * Para testar endpoints protegidos:
 * <ol>
 *   <li>Chame {@code POST /v1/auth/login} através do Swagger UI.</li>
 *   <li>O browser armazenará o cookie automaticamente.</li>
 *   <li>Todas as chamadas subsequentes incluirão o cookie.</li>
 * </ol>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuração principal da API: título, descrição, versão, contato e esquema de segurança.
     *
     * <p>O esquema {@code cookieAuth} indica que a autenticação usa o cookie
     * {@code auth_token} gerado pelo endpoint de login.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DiárioDigital API")
                        .description("""
                                Backend da plataforma **DiárioDigital** — sistema de gestão escolar
                                para a rede municipal de Cruzeiro/SP.

                                ### Autenticação
                                A API usa **JWT em cookie HttpOnly** (`auth_token`).
                                Para testar endpoints protegidos no Swagger UI:
                                1. Execute `POST /v1/auth/login` com suas credenciais.
                                2. O cookie será definido automaticamente no browser.
                                3. As chamadas subsequentes serão autenticadas automaticamente.

                                ### Controle de Acesso (RBAC)
                                Perfis disponíveis (em ordem de privilégio):
                                `ADMINISTRADOR > DIRETOR > COORDENADOR > PROFESSOR > RESPONSAVEL > ALUNO`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DiárioDigital")
                                .email("contato@diariodigital.com.br")))
                // Aplica o esquema de segurança globalmente a todos os endpoints
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("auth_token")
                                .description(
                                        "Token JWT armazenado em cookie HttpOnly. " +
                                        "Faça login em `POST /v1/auth/login` para receber o cookie.")));
    }

    /**
     * Agrupa todos os endpoints sob o prefixo {@code /v1}.
     * Útil para versionar a API futuramente.
     */
    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/v1/**")
                .build();
    }
}
