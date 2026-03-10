# INSTRUCTIONS.md — DiárioDigital Backend
> Este arquivo é o guia de referência para o Claude Code.
> Leia este arquivo integralmente antes de qualquer ação no projeto.

---

## 1. Visão Geral do Projeto

**Nome:** DiárioDigital
**Objetivo:** Digitalizar o diário de classe dos professores da rede municipal de Cruzeiro/SP, evoluindo para um sistema completo de gestão escolar.
**Problema resolvido:** Professores usam diário físico (sem rasuras, propenso a erros). Dados são transcritos manualmente para planilhas Excel no fim do ano. O sistema elimina esse fluxo.

**Stack do Backend:**
- Linguagem: Java 17+
- Framework: Spring Boot 3.x
- Banco de dados: PostgreSQL
- ORM: Spring Data JPA + Hibernate
- Migrations: Flyway
- Segurança: Spring Security + JWT
- Documentação: SpringDoc OpenAPI (Swagger UI)
- Testes: JUnit 5 + Mockito + MockMvc
- Build: Maven

---

## 2. Arquitetura Obrigatória

### 2.1 Padrão: Package by Feature

**Nunca organizar por camada técnica** (não fazer pasta `/controllers`, `/services`, `/repositories` na raiz).
Cada funcionalidade é um pacote isolado e autocontido.

```
src/main/java/br/com/diario/
│
├── modules/
│   ├── aluno/
│   │   ├── AlunoController.java
│   │   ├── AlunoService.java
│   │   ├── AlunoRepository.java
│   │   ├── Aluno.java                  ← Entity JPA
│   │   └── dto/
│   │       ├── AlunoRequestDTO.java
│   │       └── AlunoResponseDTO.java
│   │
│   ├── turma/
│   ├── frequencia/
│   ├── avaliacao/
│   ├── calendario/
│   ├── instituicao/
│   ├── pessoa/
│   └── auth/
│       ├── AuthController.java
│       ├── AuthService.java
│       └── dto/
│           ├── LoginRequestDTO.java
│           └── TokenResponseDTO.java
│
├── shared/
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   ├── JwtAuthFilter.java
│   │   ├── JwtService.java
│   │   └── UserDetailsServiceImpl.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java  ← @ControllerAdvice
│   │   ├── ResourceNotFoundException.java
│   │   ├── BusinessException.java
│   │   └── UnauthorizedException.java
│   └── dto/
│       └── ApiResponse.java             ← Wrapper padrão de resposta
│
└── config/
    ├── CorsConfig.java
    ├── OpenApiConfig.java
    └── ModelMapperConfig.java           ← ou MapStructConfig.java
```

### 2.2 Camadas Dentro de Cada Módulo

```
Controller → Service → Repository → Entity
               ↓
             DTOs (entrada e saída separados)
```

- **Controller:** Recebe requisição, valida com `@Valid`, chama o Service, retorna `ApiResponse<T>`.
- **Service:** Contém toda a regra de negócio. Nunca coloca regra no Controller ou Repository.
- **Repository:** Apenas interface `JpaRepository` + queries customizadas com `@Query` quando necessário.
- **Entity:** Mapeamento JPA. Nunca exposta diretamente na API — sempre converter para DTO.
- **DTO:** Classes separadas para entrada (`RequestDTO`) e saída (`ResponseDTO`).

---

## 3. Padrões de Código Obrigatórios

### 3.1 Resposta Padrão da API

Toda resposta da API deve usar o wrapper `ApiResponse<T>`:

```java
// ApiResponse.java
public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    Object error
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(false, null, null, message);
    }
}
```

Exemplo de uso no Controller:
```java
// Sucesso
return ResponseEntity.ok(ApiResponse.ok(alunoService.buscarPorId(id)));

// Criação
return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(dto, "Aluno cadastrado com sucesso"));
```

### 3.2 Campos de Auditoria (OBRIGATÓRIO em todas as entidades)

Todas as entidades devem estender `BaseEntity`:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;  // soft delete

    @Column(nullable = false)
    private Boolean ativo = true;
}
```

Ativar auditoria em `DiarioApplication.java`:
```java
@EnableJpaAuditing
```

### 3.3 Soft Delete (nunca deletar fisicamente)

```java
// No Service, nunca usar repository.delete()
public void desativar(Long id) {
    Aluno aluno = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    aluno.setAtivo(false);
    aluno.setDeletedAt(LocalDateTime.now());
    repository.save(aluno);
}

// No Repository, sempre filtrar por ativo
@Query("SELECT a FROM Aluno a WHERE a.ativo = true")
List<Aluno> findAllAtivos();
```

### 3.4 Validação de DTOs

```java
public record AlunoRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento,

    @NotBlank @Email(message = "E-mail inválido")
    String email
) {}
```

### 3.5 Tratamento Global de Exceções

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiResponse.error(ex.getMessage()));
    }
}
```

### 3.6 Configuração de Ambiente (application.yml)

```yaml
# application.yml (base - não contém dados sensíveis)
spring:
  profiles:
    active: dev
  jpa:
    open-in-view: false

# application-dev.yml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/diario_dev}
    username: ${DB_USER:postgres}
    password: ${DB_PASS:postgres}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

# Nunca colocar senhas reais no yml versionado.
# Sempre usar variáveis de ambiente.
```

---

## 4. Segurança e Autenticação

### 4.1 Fluxo JWT

```
POST /v1/auth/login
  → Valida usuário/senha
  → Gera access_token (expira em 15 min)
  → Gera refresh_token (expira em 7 dias)
  → Retorna ambos no body

POST /v1/auth/refresh
  → Recebe refresh_token
  → Retorna novo access_token

Todas as rotas protegidas:
  → Recebem header: Authorization: Bearer <access_token>
  → JwtAuthFilter valida e injeta o usuário no SecurityContext
```

### 4.2 RBAC — Controle de Acesso por Perfil

Perfis disponíveis (enum `TipoPessoa`):
```
ADMINISTRADOR → acesso total
DIRETOR       → visualização total, sem edição pedagógica
COORDENADOR   → gerencia turmas, professores, calendário
PROFESSOR     → acesso apenas às suas turmas/classes
ALUNO         → apenas leitura dos seus dados
RESPONSAVEL   → apenas leitura dos dados do aluno vinculado
```

Aplicar nas rotas com `@PreAuthorize`:
```java
@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('COORDENADOR')")
@PostMapping
public ResponseEntity<?> criar(@Valid @RequestBody AlunoRequestDTO dto) { ... }

@PreAuthorize("hasRole('PROFESSOR') and @alunoSecurity.pertenceAsuaTurma(#id, authentication)")
@GetMapping("/{id}")
public ResponseEntity<?> buscar(@PathVariable Long id) { ... }
```

Ativar no `SecurityConfig`:
```java
@EnableMethodSecurity
```

---

## 5. Banco de Dados e Migrations

### 5.1 Flyway — Nomenclatura obrigatória

```
src/main/resources/db/migration/
├── V1__create_pessoa_tables.sql
├── V2__create_instituicao_tables.sql
├── V3__create_calendario_tables.sql
├── V4__create_classe_turma_tables.sql
├── V5__create_frequencia_avaliacao_tables.sql
└── V6__add_audit_fields.sql
```

**Regra:** Nunca alterar um arquivo de migration já commitado. Sempre criar um novo.

### 5.2 Campos obrigatórios em todas as tabelas

```sql
created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
deleted_at  TIMESTAMP,
ativo       BOOLEAN NOT NULL DEFAULT TRUE
```

### 5.3 Módulos do Banco (referência)

| Grupo | Tabelas |
|---|---|
| Pessoas | Pessoa, Tipo_Pessoa, Contato, Contato_Pessoa, Endereco, Endereco_Pessoa, Pessoa_Responsavel |
| Calendário | Calendario_Escolar, Mes, Ano_Calendario, Periodo |
| Pedagógico | Classe, Turma, Aluno_Turma, Componente_Curricular, Disciplina |
| Avaliação | Avaliacao, Aluno_Avaliacao |
| Frequência | Aluno_Frequencia |
| Instituição | Instituicao_Ensino, Curso, Ensino (4.1), Grau (4.2), Serie (4.3) |
| Turno | Turno |

---

## 6. Convenções da API REST

### 6.1 Rotas — Padrão obrigatório

```
Prefixo base: /v1

GET    /v1/{recurso}          → listar (com paginação)
GET    /v1/{recurso}/{id}     → buscar por ID
POST   /v1/{recurso}          → criar
PUT    /v1/{recurso}/{id}     → atualizar completo
PATCH  /v1/{recurso}/{id}     → atualizar parcial
DELETE /v1/{recurso}/{id}     → soft delete (desativar)
```

### 6.2 Rotas Prioritárias

```
/v1/auth/login
/v1/auth/refresh
/v1/auth/logout

/v1/alunos
/v1/alunos/{id}/frequencia
/v1/alunos/{id}/notas
/v1/alunos/{id}/boletim

/v1/turmas
/v1/turmas/{id}/alunos         → matricular / listar alunos
/v1/turmas/{id}/frequencia     → frequência consolidada da turma

/v1/frequencia                 → lançar frequência de uma aula
/v1/avaliacoes
/v1/avaliacoes/{id}/notas      → lançar notas em lote

/v1/calendarios
/v1/classes
/v1/instituicoes
/v1/relatorios/boletim/{alunoId}
/v1/relatorios/turma/{id}
```

### 6.3 Paginação padrão

```java
// Sempre usar Pageable nas listagens
@GetMapping
public ResponseEntity<ApiResponse<Page<AlunoResponseDTO>>> listar(
    @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
    return ResponseEntity.ok(ApiResponse.ok(alunoService.listar(pageable)));
}
```

---

## 7. Regras de Negócio — Implementar no Service

### 7.1 Frequência

- Frequência mínima legal: **75%** (LDB). Calcular e expor no endpoint.
- Ao registrar uma aula, criar automaticamente registros para **todos** os alunos da turma.
- Tipos: `PRESENTE`, `FALTA`, `FALTA_JUSTIFICADA`.
- `FALTA_JUSTIFICADA` não conta para reprovação, mas conta no total de aulas.
- Fórmula: `percentual = (totalPresencas / totalAulas) * 100`

### 7.2 Avaliações e Médias

- Cada avaliação possui campo `peso` (ex: prova = 7, trabalho = 3).
- Média ponderada por período e por componente curricular.
- Situação do aluno:
  - `APROVADO`: média ≥ 5.0 **e** frequência ≥ 75%
  - `REPROVADO_NOTA`: média < 5.0
  - `REPROVADO_FREQUENCIA`: frequência < 75%
  - `EM_RECUPERACAO`: média entre 3.0 e 4.9 (configurável por instituição)

### 7.3 Acesso e Auditoria

- Professor acessa **apenas** turmas/classes às quais está vinculado na tabela `Classe`.
- Responsável acessa **apenas** dados do(s) aluno(s) vinculado(s) via `Pessoa_Responsavel`.
- Toda alteração de nota deve gerar log de auditoria: `quem alterou`, `quando`, `valor anterior`, `valor novo`.

---

## 8. Fase Atual e Próximos Passos

### Estado atual do projeto
- [x] CRUDs básicos das entidades criados
- [x] Autenticação implementada
- [ ] Estrutura de pacotes não segue Package by Feature
- [ ] RBAC não implementado
- [ ] Regras de negócio não implementadas
- [ ] Campos de auditoria ausentes nas tabelas
- [ ] Flyway não configurado
- [ ] Swagger não documentado
- [ ] Testes não implementados

### Ordem de execução (não pular etapas)

1. **Reorganizar estrutura de pacotes** → aplicar Package by Feature (seção 2.1)
2. **Adicionar BaseEntity** com campos de auditoria em todas as entidades
3. **Configurar Flyway** → criar scripts de migration para o schema atual
4. **Implementar `ApiResponse<T>`** e `GlobalExceptionHandler`
5. **Implementar RBAC completo** → Spring Security + `@PreAuthorize` por rota
6. **Implementar fluxo de matrícula** → vincular aluno a turma (`Aluno_Turma`)
7. **Implementar lançamento de frequência** com cálculo de percentual
8. **Implementar lançamento de notas** com cálculo de média ponderada
9. **Configurar SpringDoc/Swagger** e documentar todas as rotas
10. **Escrever testes** nas rotas de auth e nas regras de negócio

---

## 9. O Que Nunca Fazer

- ❌ Nunca retornar uma entidade JPA diretamente na resposta da API — sempre converter para DTO
- ❌ Nunca colocar regra de negócio no Controller — pertence ao Service
- ❌ Nunca deletar registros fisicamente — usar soft delete (`ativo = false`, `deletedAt`)
- ❌ Nunca commitar senhas, URLs ou tokens no código — usar variáveis de ambiente
- ❌ Nunca alterar um arquivo de migration Flyway já versionado — criar um novo
- ❌ Nunca usar `ddl-auto: create` ou `ddl-auto: create-drop` fora do ambiente de testes
- ❌ Nunca commitar diretamente na branch `main` — todo código passa por PR na `develop`
- ❌ Nunca lançar `Exception` genérica — usar `ResourceNotFoundException` ou `BusinessException`

---

## 10. Git Flow

```
main      → código estável, pronto para produção
develop   → integração contínua entre os dois devs
feature/* → ex: feature/rbac-spring-security
fix/*     → ex: fix/calculo-media-ponderada
chore/*   → ex: chore/configurar-flyway
```

**Regra:** Todo PR deve ser revisado pelo outro dev antes do merge na `develop`.

---

*Última atualização: 2025 — Versão 1.2 (Backend Java + Spring Boot)*

---

## 11. Padrão de Comentários e Documentação do Código

> Este projeto tem dupla finalidade: **uso comercial real** e **aprendizado**.
> Por isso, todo código deve ser comentado de forma clara, explicando não apenas **o que** faz, mas **por que** foi feito assim.

### 11.1 Regra Geral

- Todo método público de `Service` e `Controller` deve ter Javadoc (`/** */`).
- Lógicas complexas ou não óbvias dentro dos métodos devem ter comentário inline (`//`).
- Decisões de arquitetura ou regras de negócio relevantes devem ter comentário explicativo antes do bloco.
- Não comentar o óbvio — `// incrementa i` em `i++` é ruído, não documentação.

---

### 11.2 Javadoc — Controllers

Documentar o propósito do endpoint, o que recebe e o que retorna:

```java
/**
 * Endpoint para cadastrar um novo aluno na instituição.
 *
 * Apenas usuários com perfil ADMINISTRADOR, COORDENADOR ou PROFESSOR
 * têm permissão para realizar esta operação (definido via @PreAuthorize).
 *
 * @param dto Dados do aluno vindos do corpo da requisição, validados com @Valid
 * @return ApiResponse contendo os dados do aluno criado e status HTTP 201
 */
@PostMapping
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR')")
public ResponseEntity<ApiResponse<AlunoResponseDTO>> criar(@Valid @RequestBody AlunoRequestDTO dto) {
    AlunoResponseDTO response = alunoService.criar(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, "Aluno cadastrado com sucesso"));
}
```

---

### 11.3 Javadoc — Services

Documentar a regra de negócio, não apenas o que o método faz tecnicamente:

```java
/**
 * Calcula o percentual de frequência de um aluno em uma turma.
 *
 * Regra (LDB, Art. 24): o aluno precisa de no mínimo 75% de presença
 * para não ser reprovado por frequência, independente do desempenho nas notas.
 *
 * Faltas justificadas são contabilizadas no total de aulas, mas NÃO
 * contam como falta para efeito de reprovação.
 *
 * Fórmula: percentual = (totalPresencas / totalAulas) * 100
 *
 * @param alunoId  ID do aluno a ser consultado
 * @param turmaId  ID da turma de referência
 * @return FrequenciaResponseDTO com total de aulas, presenças, faltas e percentual
 * @throws ResourceNotFoundException se aluno ou turma não forem encontrados
 */
public FrequenciaResponseDTO calcularFrequencia(Long alunoId, Long turmaId) {
    // Busca o aluno — lança exceção personalizada se não existir
    Aluno aluno = alunoRepository.findById(alunoId)
        .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + alunoId));

    // Busca todos os registros de frequência do aluno nesta turma
    List<AlunoFrequencia> registros = frequenciaRepository.findByAlunoIdAndTurmaId(alunoId, turmaId);

    long totalAulas = registros.size();

    // Presenças = registros com tipo PRESENTE
    long totalPresencas = registros.stream()
        .filter(f -> f.getTipo() == TipoFrequencia.PRESENTE)
        .count();

    // Faltas justificadas não contam para reprovação, por isso são separadas
    long totalFaltasJustificadas = registros.stream()
        .filter(f -> f.getTipo() == TipoFrequencia.FALTA_JUSTIFICADA)
        .count();

    // Evita divisão por zero caso nenhuma aula tenha sido registrada ainda
    double percentual = totalAulas > 0 ? (double) totalPresencas / totalAulas * 100 : 0;

    return new FrequenciaResponseDTO(totalAulas, totalPresencas, totalFaltasJustificadas, percentual);
}
```

---

### 11.4 Javadoc — Entities

Documentar o propósito da entidade e os campos com regras importantes:

```java
/**
 * Representa um aluno cadastrado no sistema.
 *
 * Um aluno é uma especialização de Pessoa (identificada pelo tipo ALUNO
 * na tabela Tipo_Pessoa). A vinculação do aluno a turmas é feita pela
 * entidade AlunoTurma (tabela aluno_turma).
 *
 * Soft delete: ao invés de excluir, o campo 'ativo' é definido como false
 * e 'deletedAt' recebe a data da desativação. Isso preserva o histórico
 * pedagógico do aluno.
 */
@Entity
@Table(name = "pessoa")
public class Aluno extends BaseEntity {

    /** Matrícula única do aluno gerada pela instituição */
    @Column(nullable = false, unique = true)
    private String matricula;

    /**
     * Relacionamento com o responsável legal.
     * Um aluno pode ter mais de um responsável (ex: pai e mãe).
     */
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<PessoaResponsavel> responsaveis;
}
```

---

### 11.5 Comentários Inline — Quando e Como Usar

Use comentários `//` dentro dos métodos para explicar decisões não óbvias:

```java
// ✅ BOM — explica o porquê de uma decisão
// Spring Security exige o prefixo "ROLE_" internamente,
// mas nas anotações @PreAuthorize usamos sem o prefixo por convenção
authorities.add(new SimpleGrantedAuthority("ROLE_" + tipoPessoa.name()));

// ✅ BOM — explica uma regra de negócio embutida no código
// Média de recuperação: entre 3.0 e 4.9 o aluno vai para recuperação.
// Abaixo de 3.0 é reprovação direta, sem recuperação (regra da instituição).
if (media >= 3.0 && media < 5.0) {
    return SituacaoAluno.EM_RECUPERACAO;
}

// ❌ RUIM — comenta o óbvio, não agrega nada
// Salva o aluno no banco
alunoRepository.save(aluno);
```

---

### 11.6 Documentação do Swagger com SpringDoc

Todo endpoint deve ter anotações para gerar documentação clara no Swagger UI:

```java
@Operation(
    summary = "Lançar frequência de uma aula",
    description = """
        Registra a presença ou falta de todos os alunos de uma turma em uma aula específica.
        Ao chamar este endpoint, registros são criados para TODOS os alunos da turma.
        Apenas o professor vinculado à classe pode lançar a frequência.
        """
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Frequência registrada com sucesso"),
    @ApiResponse(responseCode = "403", description = "Professor não autorizado para esta turma"),
    @ApiResponse(responseCode = "404", description = "Turma ou calendário não encontrado"),
    @ApiResponse(responseCode = "422", description = "Aula já possui frequência registrada")
})
@PostMapping
public ResponseEntity<ApiResponse<FrequenciaResponseDTO>> lancarFrequencia(
    @Valid @RequestBody FrequenciaRequestDTO dto) { ... }
```

---

### 11.7 Resumo — Checklist de Comentários por Tipo de Arquivo

| Arquivo | O que documentar |
|---|---|
| `Controller` | Javadoc em todo endpoint: permissão, parâmetros, retorno |
| `Service` | Javadoc em todo método público: regra de negócio, fórmulas, exceções |
| `Entity` | Javadoc na classe e nos campos com regras relevantes |
| `Repository` | Javadoc nas queries customizadas (`@Query`) explicando o critério |
| `Config` | Comentário explicando por que aquela configuração existe |
| Lógica complexa | Comentário inline `//` antes do bloco, explicando o porquê |
| Swagger | `@Operation` e `@ApiResponse` em todos os endpoints |

