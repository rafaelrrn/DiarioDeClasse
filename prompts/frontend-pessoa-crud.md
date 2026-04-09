# Prompt para o Claude Code — Frontend: CRUD do domínio Pessoa

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Pessoa** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Contexto do domínio

O domínio `pessoa` representa qualquer indivíduo do sistema: alunos, professores, responsáveis, coordenadores. O backend usa um modelo normalizado — **Contato** e **Endereço** são entidades próprias, vinculadas à Pessoa por tabelas de junção (`ContatoPessoa` e `EnderecoPessoa`). **PessoaResponsavel** vincula dois registros de Pessoa (aluno ↔ responsável).

O backend usa o **Extended Party Pattern**: dados específicos de cada perfil ficam em tabelas separadas (`aluno_perfil`, `professor_perfil`), vinculadas à Pessoa por relação 1:1.

Há 9 recursos no total:

**Operacional:** Pessoa, ContatoPessoa, EnderecoPessoa, PessoaResponsavel, AlunoPerfil, ProfessorPerfil
**Cadastro de apoio:** TipoPessoa, Contato (globais), Endereco (globais)

---

## Endpoints e DTOs

### 1. Pessoa — `/v1/pessoas`

```typescript
interface PessoaDTO {
  idPessoa?: number;
  idTipoPessoa: number;       // obrigatório — FK para TipoPessoa
  nome: string;               // obrigatório, max 255
  cpf?: string;               // opcional, exatamente 11 dígitos numéricos (sem máscara)
  sexo?: 'M' | 'F' | 'NB' | 'NI'; // M=Masculino, F=Feminino, NB=Não-binário, NI=Não-informado
  dataNascimento?: string;    // opcional, formato "YYYY-MM-DD" — backend armazena como DATE
  situacao?: 'ATIVO' | 'INATIVO' | 'TRANSFERIDO' | 'EVADIDO' | 'FORMADO';
  fotoUrl?: string;           // opcional, max 500 — URL da foto
  obs?: string;               // texto livre, max 255
}
```

> **`sexo`**: os valores aceitos pelo backend são exatamente `'M'`, `'F'`, `'NB'`, `'NI'` (CHECK constraint). Exibir labels amigáveis no UI mas enviar os códigos.
>
> **`situacao`**: os valores aceitos são exatamente `'ATIVO'`, `'INATIVO'`, `'TRANSFERIDO'`, `'EVADIDO'`, `'FORMADO'` (CHECK constraint).
>
> **`cpf`**: enviar sem máscara (somente 11 dígitos). Exibir com máscara `999.999.999-99` na tela.
>
> **`dataNascimento`**: o backend armazena como `DATE` (LocalDate). Enviar sempre no formato `"YYYY-MM-DD"`. Exibir como `"DD/MM/YYYY"`.

| Método | Rota | Roles |
|--------|------|-------|
| GET | `/v1/pessoas` | ADMINISTRADOR, COORDENADOR, DIRETOR |
| GET | `/v1/pessoas/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR |
| POST | `/v1/pessoas` | ADMINISTRADOR, COORDENADOR |
| PUT | `/v1/pessoas/{id}` | ADMINISTRADOR, COORDENADOR |
| DELETE | `/v1/pessoas/{id}` | ADMINISTRADOR |

---

### 2. AlunoPerfil — `/v1/aluno-perfil`

Perfil de dados específicos do aluno. Relação 1:1 com Pessoa — cada Pessoa pode ter no máximo um perfil de aluno.

```typescript
interface AlunoPerfilDTO {
  idAlunoPerfil?: number;
  idPessoa: number;               // obrigatório — FK para Pessoa
  matricula: string;              // obrigatório, max 30, único no sistema
  dataMatricula: string;          // obrigatório, formato "YYYY-MM-DD"
  necessidadeEspecial: boolean;   // default false
  descricaoNee?: string;          // texto livre — obrigatório se necessidadeEspecial = true
}
```

| Método | Rota | Roles |
|--------|------|-------|
| GET | `/v1/aluno-perfil` | ADMINISTRADOR, COORDENADOR |
| GET | `/v1/aluno-perfil/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR |
| GET | `/v1/aluno-perfil/pessoa/{idPessoa}` | ADMINISTRADOR, COORDENADOR, PROFESSOR |
| POST | `/v1/aluno-perfil` | ADMINISTRADOR, COORDENADOR |
| PUT | `/v1/aluno-perfil/{id}` | ADMINISTRADOR, COORDENADOR |
| DELETE | `/v1/aluno-perfil/{id}` | ADMINISTRADOR |

> ⚠️ **Regras de negócio:**
> - O backend retorna **422** se a Pessoa já tiver um perfil de aluno
> - O backend retorna **422** se a matrícula já estiver em uso
> - O vínculo com Pessoa (`idPessoa`) **não pode ser alterado** via PUT — apenas os campos de perfil

---

### 3. ProfessorPerfil — `/v1/professor-perfil`

Perfil de dados específicos do professor. Relação 1:1 com Pessoa.

```typescript
interface ProfessorPerfilDTO {
  idProfessorPerfil?: number;
  idPessoa: number;         // obrigatório — FK para Pessoa
  registroMec?: string;     // opcional, max 30
  formacao?: string;        // opcional, max 200
  dataAdmissao: string;     // obrigatório, formato "YYYY-MM-DD"
}
```

| Método | Rota | Roles |
|--------|------|-------|
| GET | `/v1/professor-perfil` | ADMINISTRADOR, COORDENADOR |
| GET | `/v1/professor-perfil/{id}` | ADMINISTRADOR, COORDENADOR |
| GET | `/v1/professor-perfil/pessoa/{idPessoa}` | ADMINISTRADOR, COORDENADOR |
| POST | `/v1/professor-perfil` | ADMINISTRADOR, COORDENADOR |
| PUT | `/v1/professor-perfil/{id}` | ADMINISTRADOR, COORDENADOR |
| DELETE | `/v1/professor-perfil/{id}` | ADMINISTRADOR |

> ⚠️ O backend retorna **422** se a Pessoa já tiver um perfil de professor.

---

### 4. TipoPessoa — `/v1/tipos-pessoa`

```typescript
interface TipoPessoaDTO {
  idTipoPessoa?: number;
  nome: string; // obrigatório, max 255 — ex: "Aluno", "Professor", "Responsável"
}
```

Todos os endpoints exigem **apenas ADMINISTRADOR** (sem COORDENADOR).

---

### 5. Contato — `/v1/contatos`

```typescript
interface ContatoDTO {
  idContato?: number;
  tipoContato: string; // obrigatório, max 255 — ex: "Celular", "E-mail"
  contato: string;     // obrigatório, max 255 — o valor em si
}
```

Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

---

### 6. ContatoPessoa — `/v1/contatos-pessoa`

```typescript
interface ContatoPessoaDTO {
  idContatoPessoa?: number;
  idPessoa: number;
  idContato: number;
  nome?: string; // apelido da associação, ex: "Celular pessoal"
}
```

> ⚠️ **PUT só altera `nome`** — não é possível trocar `idPessoa` ou `idContato`.

---

### 7. Endereco — `/v1/enderecos`

```typescript
interface EnderecoDTO {
  idEndereco?: number;
  uf?: string;          // max 2
  cidade?: string;
  bairro?: string;
  rua?: string;
  numero?: string;      // max 10
  cep?: string;         // max 9
  complemento?: string;
}
```

---

### 8. EnderecoPessoa — `/v1/enderecos-pessoa`

```typescript
interface EnderecoPessoaDTO {
  idEnderecoPessoa?: number;
  idPessoa: number;
  idEndereco: number;
  nome?: string; // apelido, ex: "Residencial"
}
```

> ⚠️ **PUT só altera `nome`**.

---

### 9. PessoaResponsavel — `/v1/pessoas-responsavel`

```typescript
interface PessoaResponsavelDTO {
  idPessoaResponsavel?: number;
  idAluno: number;       // FK para Pessoa (o aluno)
  idResponsavel: number; // FK para Pessoa (o responsável)
  parentesco?: string;
}
```

> ⚠️ **PUT só altera `parentesco`**. Ambos `idAluno` e `idResponsavel` são IDs de Pessoa.

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/pessoa/
├── types.ts              ← 9 DTOs TypeScript
├── pessoaService.ts      ← funções Axios para todos os 9 recursos
└── pessoaQueries.ts      ← hooks TanStack Query
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── pessoas/
│   ├── page.tsx              ← Lista de pessoas com filtro por tipo
│   └── [id]/
│       └── page.tsx          ← Detalhe: Dados | Perfil | Contatos | Endereços | Responsáveis
│
└── admin/
    └── pessoa/
        └── page.tsx          ← Tabs: TipoPessoa | Contatos | Endereços
```

---

## Tela 1 — Lista de Pessoas (`/pessoas`)

**Conteúdo:**
- `PageHeader` com título "Pessoas" e botão **+ Nova Pessoa** (ADMINISTRADOR, COORDENADOR)
- `Table` com colunas: **Nome** | **CPF** | **Tipo** | **Situação** | **Ações**
  - Coluna "CPF": exibir com máscara `999.999.999-99` ou "—" se nulo
  - Coluna "Situação": `Badge` colorido — ATIVO (verde), INATIVO (cinza), TRANSFERIDO (azul), EVADIDO (amarelo), FORMADO (roxo)
- Filtro por tipo: `Select` com opções de `GET /v1/tipos-pessoa` (filtro client-side)
- Ações por linha: **Ver detalhes** (→ `/pessoas/[id]`) | editar | excluir (ADMINISTRADOR)
- Dialog de criar/editar com todos os campos de `PessoaDTO`
- `AlertDialog` de confirmação antes de excluir

**Dialog de criar/editar Pessoa:**

| Campo | Componente | Detalhe |
|-------|-----------|---------|
| Tipo de Pessoa | `Select` | obrigatório, lookup `GET /v1/tipos-pessoa` |
| Nome | `Input` | obrigatório |
| CPF | `Input` | opcional, máscara `999.999.999-99`, enviar sem máscara (11 dígitos) |
| Sexo | `Select` | opções: `{ value: 'M', label: 'Masculino' }`, `F/Feminino`, `NB/Não-binário`, `NI/Não-informado` |
| Data de Nascimento | `Input type="date"` | enviar como `YYYY-MM-DD` |
| Situação | `Select` | opções: `ATIVO`, `INATIVO`, `TRANSFERIDO`, `EVADIDO`, `FORMADO` (labels amigáveis) |
| Foto (URL) | `Input` | opcional, placeholder com exemplo de URL |
| Observação | `Textarea` | opcional |

---

## Tela 2 — Detalhe da Pessoa (`/pessoas/[id]`)

Organizada em **5 Cards** via `Card` do shadcn/ui.

### Card 1 — Dados da Pessoa

Exibir todos os campos: Nome, CPF (com máscara), Tipo, Sexo (label amigável), Nascimento (DD/MM/YYYY), Situação (Badge), Foto (thumbnail se preenchida), Obs.

Botões: **Editar** (ADMINISTRADOR, COORDENADOR) | **← Voltar**

---

### Card 2 — Perfil Específico

Este card é condicional ao tipo de Pessoa:

**Se a Pessoa for do tipo "Aluno"** — exibir dados de `AlunoPerfil`:

Chamar `GET /v1/aluno-perfil/pessoa/{idPessoa}`. Se retornar 404, exibir botão **+ Criar Perfil de Aluno**.

| Campo exibido | Dado |
|---|---|
| Matrícula | `matricula` |
| Data de Matrícula | `dataMatricula` (DD/MM/YYYY) |
| Necessidade Especial | Badge "Sim"/"Não" |
| Descrição NEE | exibir se `necessidadeEspecial = true` |

Dialog de criar/editar AlunoPerfil:

```typescript
const schemaAlunoPerfil = z.object({
  matricula: z.string().min(1).max(30),
  dataMatricula: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  necessidadeEspecial: z.boolean().default(false),
  descricaoNee: z.string().optional(),
}).refine(
  (d) => !d.necessidadeEspecial || (d.descricaoNee && d.descricaoNee.length > 0),
  { message: 'Descreva a necessidade especial', path: ['descricaoNee'] }
);
```

`idPessoa` é preenchido automaticamente com o ID da Pessoa da rota — não exibir campo para o usuário.

**Se a Pessoa for do tipo "Professor"** — exibir dados de `ProfessorPerfil`:

Chamar `GET /v1/professor-perfil/pessoa/{idPessoa}`. Se retornar 404, exibir botão **+ Criar Perfil de Professor**.

| Campo exibido | Dado |
|---|---|
| Registro MEC | `registroMec` ou "—" |
| Formação | `formacao` ou "—" |
| Data de Admissão | `dataAdmissao` (DD/MM/YYYY) |

Dialog de criar/editar ProfessorPerfil:

```typescript
const schemaProfessorPerfil = z.object({
  registroMec: z.string().max(30).optional(),
  formacao: z.string().max(200).optional(),
  dataAdmissao: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Data inválida'),
});
```

**Para outros tipos de Pessoa** (Responsável, Coordenador etc.) — não exibir este card.

**Como detectar o tipo:** comparar `pessoa.idTipoPessoa` com os IDs retornados por `GET /v1/tipos-pessoa`. Usar o campo `nome` (case-insensitive) para identificar se é "Aluno" ou "Professor".

---

### Card 3 — Contatos Vinculados

**Carregamento:** `GET /v1/contatos-pessoa` → filtrar client-side por `idPessoa === id`

**Tabela:** Apelido (`nome`) | Tipo | Valor | Ações

**Botão "+ Vincular Contato"** — Dialog com:
- `Select` de Contato existente (lookup `GET /v1/contatos`, exibir `tipoContato + ": " + contato`)
- `Input` de Apelido (`nome`) — opcional

**Ação editar:** abre Dialog apenas com campo `nome` (único campo editável via PUT)

**Ação desvincular:** `AlertDialog` + `DELETE /v1/contatos-pessoa/{id}`

---

### Card 4 — Endereços Vinculados

**Carregamento:** `GET /v1/enderecos-pessoa` → filtrar client-side por `idPessoa === id`

**Tabela:** Apelido | CEP | Rua | Número | Cidade/UF | Ações

**Botão "+ Vincular Endereço"** — Dialog com:
- `Select` de Endereço existente (exibir `rua, numero - cidade/UF` ou CEP)
- `Input` de Apelido (`nome`) — opcional

**Ação editar:** Dialog apenas com `nome`

**Ação desvincular:** `AlertDialog` + `DELETE /v1/enderecos-pessoa/{id}`

---

### Card 5 — Responsáveis (relevante para alunos)

**Carregamento:** `GET /v1/pessoas-responsavel` → filtrar client-side por `idAluno === id`

**Tabela:** Nome Responsável | Parentesco | Ações

**Botão "+ Vincular Responsável"** — Dialog com:
- `Select` de Pessoa — lookup `GET /v1/pessoas`, exibir `nome`
- `Input` de Parentesco — opcional

**Ação editar:** Dialog apenas com `parentesco`

**Ação desvincular:** `AlertDialog` + `DELETE /v1/pessoas-responsavel/{id}`

---

## Tela 3 — Configurações Administrativas (`/admin/pessoa`)

Página com **Tabs** em 3 abas:

### Aba 1 — Tipos de Pessoa
> ⚠️ Apenas **ADMINISTRADOR**.

- `Table`: ID | Nome | Ações
- Dialog: campo `nome` obrigatório
- `AlertDialog` exclusão

### Aba 2 — Contatos
- `Table`: ID | Tipo | Valor | Ações
- Dialog criar/editar: `Select` de Tipo (opções sugeridas + campo livre) + `Input` de Valor
- `AlertDialog` exclusão

### Aba 3 — Endereços
- `Table`: ID | CEP | Rua | Número | Cidade | UF | Ações
- Dialog criar/editar com todos os campos do `EnderecoDTO`
- `AlertDialog` exclusão

---

## Controle de acesso

```typescript
const { hasAny, is } = useRoles();

// Botões de criar/editar — ADMIN e COORD
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button>+ Nova Pessoa</Button>}

// Excluir — apenas ADMIN
{is('ADMINISTRADOR') && <Button variant="ghost">Excluir</Button>}

// Aba TipoPessoa — apenas ADMIN
{is('ADMINISTRADOR') ? <TipoPessoaTab /> : (
  <p className="text-muted-foreground">Acesso restrito ao Administrador.</p>
)}
```

---

## Validação Zod por recurso

```typescript
const SEXO_OPTIONS = [
  { value: 'M',  label: 'Masculino'    },
  { value: 'F',  label: 'Feminino'     },
  { value: 'NB', label: 'Não-binário'  },
  { value: 'NI', label: 'Não-informado'},
] as const;

const SITUACAO_OPTIONS = [
  { value: 'ATIVO',        label: 'Ativo'        },
  { value: 'INATIVO',      label: 'Inativo'      },
  { value: 'TRANSFERIDO',  label: 'Transferido'  },
  { value: 'EVADIDO',      label: 'Evadido'      },
  { value: 'FORMADO',      label: 'Formado'      },
] as const;

// Pessoa
const schemaPessoa = z.object({
  idTipoPessoa: z.coerce.number({ required_error: 'Tipo é obrigatório' }),
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
  cpf: z.string().length(11, 'CPF deve ter 11 dígitos').regex(/^\d+$/, 'Apenas números').optional().or(z.literal('')),
  sexo: z.enum(['M', 'F', 'NB', 'NI']).optional(),
  dataNascimento: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Use o formato YYYY-MM-DD').optional().or(z.literal('')),
  situacao: z.enum(['ATIVO','INATIVO','TRANSFERIDO','EVADIDO','FORMADO']).optional(),
  fotoUrl: z.string().url('URL inválida').max(500).optional().or(z.literal('')),
  obs: z.string().max(255).optional(),
});

// AlunoPerfil
const schemaAlunoPerfil = z.object({
  matricula: z.string().min(1, 'Matrícula é obrigatória').max(30),
  dataMatricula: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Data inválida'),
  necessidadeEspecial: z.boolean().default(false),
  descricaoNee: z.string().optional(),
}).refine(
  (d) => !d.necessidadeEspecial || !!d.descricaoNee?.trim(),
  { message: 'Descreva a necessidade especial', path: ['descricaoNee'] }
);

// ProfessorPerfil
const schemaProfessorPerfil = z.object({
  registroMec: z.string().max(30).optional(),
  formacao: z.string().max(200).optional(),
  dataAdmissao: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Data inválida'),
});

// TipoPessoa
const schemaNome = z.object({ nome: z.string().min(1).max(255) });

// Contato
const schemaContato = z.object({
  tipoContato: z.string().min(1).max(255),
  contato: z.string().min(1).max(255),
});

// ContatoPessoa (vincular)
const schemaContatoPessoa = z.object({
  idContato: z.coerce.number({ required_error: 'Contato é obrigatório' }),
  nome: z.string().max(255).optional(),
});

// Endereco
const schemaEndereco = z.object({
  uf: z.string().max(2).optional(),
  cidade: z.string().max(255).optional(),
  bairro: z.string().max(255).optional(),
  rua: z.string().max(255).optional(),
  numero: z.string().max(10).optional(),
  cep: z.string().max(9).optional(),
  complemento: z.string().max(255).optional(),
});

// EnderecoPessoa (vincular)
const schemaEnderecoPessoa = z.object({
  idEndereco: z.coerce.number({ required_error: 'Endereço é obrigatório' }),
  nome: z.string().max(255).optional(),
});

// PessoaResponsavel (vincular)
const schemaPessoaResponsavel = z.object({
  idResponsavel: z.coerce.number({ required_error: 'Responsável é obrigatório' }),
  parentesco: z.string().max(255).optional(),
});
```

---

## Query keys

```typescript
['pessoas']                           // lista completa
['pessoa', id]                        // detalhe de uma pessoa
['tipos-pessoa']                      // lookup para Select
['aluno-perfil', 'pessoa', idPessoa]  // perfil de aluno de uma pessoa
['professor-perfil', 'pessoa', idPessoa] // perfil de professor de uma pessoa
['contatos']
['contatos-pessoa']
['enderecos']
['enderecos-pessoa']
['pessoas-responsavel']
['pessoas-responsavel', idAluno]
```

---

## Considerações importantes de UX

### CPF — enviar sem máscara

Usar uma lib de máscara (ex: `react-input-mask` ou `cleave.js`) para exibir `999.999.999-99`, mas ao enviar ao backend remover os pontos e traço: `cpf.replace(/\D/g, '')`. O backend armazena somente os 11 dígitos.

### sexo e situacao — enums com CHECK no banco

O backend tem `CHECK CONSTRAINT` para esses campos. Valores fora do conjunto causam erro 500. Use sempre os códigos exatos (`'M'`, `'F'`, `'NB'`, `'NI'` e `'ATIVO'` etc.) — nunca as labels.

### dataNascimento — LocalDate no backend

O backend armazena como `DATE` (não mais `String`). Enviar sempre `"YYYY-MM-DD"`. Exibir com `new Date(dataNascimento + 'T00:00:00').toLocaleDateString('pt-BR')` (sufixo de horário necessário para evitar problema de timezone).

### Card de Perfil — detectar tipo dinamicamente

```typescript
const { data: tipos } = useQuery(['tipos-pessoa'], listarTiposPessoa);

const tipoNome = tipos?.find(t => t.idTipoPessoa === pessoa.idTipoPessoa)?.nome?.toLowerCase() ?? '';

const isAluno     = tipoNome.includes('aluno');
const isProfessor = tipoNome.includes('professor');
```

### Perfil não encontrado — 404 é esperado

`GET /v1/aluno-perfil/pessoa/{id}` retorna 404 quando a Pessoa ainda não tem perfil de aluno. Tratar esse caso como estado válido (exibir botão de criação), não como erro.

```typescript
const { data: alunoPerfil, isError } = useQuery(
  ['aluno-perfil', 'pessoa', id],
  () => buscarAlunoPerfilPorPessoa(id),
  { retry: false } // não tentar novamente em 404
);
// isError com status 404 → exibir "+ Criar Perfil de Aluno"
```

### PUT nos perfis — idPessoa é imutável

O `PUT /v1/aluno-perfil/{id}` e `PUT /v1/professor-perfil/{id}` **não aceitam alteração de `idPessoa`**. O Dialog de edição não deve exibir esse campo.

---

## O que NÃO fazer

- Não enviar `sexo` com valores como `"Masculino"` — o backend rejeita (CHECK constraint). Enviar `"M"`.
- Não enviar `situacao` com `"Ativo"` — enviar `"ATIVO"` (maiúsculas).
- Não enviar `cpf` com máscara — remover formatação antes do POST/PUT.
- Não tentar editar `idContato`/`idEndereco` via PUT em ContatoPessoa/EnderecoPessoa — apenas `nome` é editável.
- Não tentar editar `idAluno`/`idResponsavel` via PUT em PessoaResponsavel — apenas `parentesco` é editável.
- Não tentar alterar `idPessoa` via PUT em AlunoPerfil/ProfessorPerfil — o vínculo é imutável.
- Não exibir a aba TipoPessoa para COORDENADOR.

---

## Checklist de entrega

- [ ] `types.ts` — 9 DTOs TypeScript com `SEXO_OPTIONS` e `SITUACAO_OPTIONS` exportados
- [ ] `pessoaService.ts` — funções de API para todos os 9 recursos
- [ ] `pessoaQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `/pessoas` — lista com filtro por tipo, badge de situação colorido, coluna CPF com máscara
- [ ] `/pessoas/[id]` — 5 Cards: Dados | Perfil Específico | Contatos | Endereços | Responsáveis
- [ ] Card Dados: exibir `cpf` com máscara, `fotoUrl` como thumbnail, `dataNascimento` como DD/MM/YYYY
- [ ] Card Perfil: detectar tipo dinamicamente; exibir AlunoPerfil ou ProfessorPerfil; tratar 404 como "sem perfil"
- [ ] Card Contatos: vincular (Select existente), editar apelido, desvincular
- [ ] Card Endereços: vincular (Select existente), editar apelido, desvincular
- [ ] Card Responsáveis: vincular (Select de Pessoa), editar parentesco, desvincular
- [ ] `/admin/pessoa` — Tabs: TipoPessoa (ADMIN only) | Contatos | Endereços
- [ ] Zod: `cpf` com `.length(11)`, `sexo` com `.enum(['M','F','NB','NI'])`, `situacao` com enum correto
- [ ] `dataNascimento`: exibir DD/MM/YYYY com sufixo `T00:00:00` para evitar bug de timezone
- [ ] `necessidadeEspecial = true` → campo `descricaoNee` obrigatório (validação Zod com `.refine`)
- [ ] 404 em perfil de aluno/professor → botão de criação (não exibir erro)
- [ ] Loading Skeleton em todas as listas
- [ ] Toast de erro com campo `error` da ApiResponse
- [ ] AlertDialog com texto claro antes de qualquer exclusão ou desvinculação
