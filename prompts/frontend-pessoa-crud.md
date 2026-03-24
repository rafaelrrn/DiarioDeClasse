# Prompt para o Claude Code — Frontend: CRUD do domínio Pessoa

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Pessoa** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Contexto do domínio

O domínio `pessoa` representa qualquer indivíduo do sistema: alunos, professores, responsáveis, coordenadores. O backend usa um modelo normalizado — **Contato** e **Endereço** são entidades próprias, vinculadas à Pessoa por tabelas de junção (`ContatoPessoa` e `EnderecoPessoa`). **PessoaResponsavel** vincula dois registros de Pessoa (aluno ↔ responsável).

Há 7 recursos no total, divididos em dois grupos:

**Operacional:** Pessoa, ContatoPessoa, EnderecoPessoa, PessoaResponsavel
**Cadastro de apoio:** TipoPessoa, Contato (globais), Endereco (globais)

---

## Endpoints e DTOs

### 1. Pessoa — `/v1/pessoas`
```typescript
interface PessoaDTO {
  idPessoa?: number;
  idTipoPessoa: number;       // obrigatório — FK para TipoPessoa
  nome: string;               // obrigatório, max 255
  sexo?: string;              // opcional: "Masculino" | "Feminino" | "Outro"
  dataNascimento?: string;    // opcional: formato "YYYY-MM-DD"
  situacao?: string;          // opcional: "Ativo" | "Inativo"
  obs?: string;               // texto livre, max 255
}
```

| Método | Rota | Roles |
|--------|------|-------|
| GET | `/v1/pessoas` | ADMINISTRADOR, COORDENADOR, DIRETOR |
| GET | `/v1/pessoas/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR |
| POST | `/v1/pessoas` | ADMINISTRADOR, COORDENADOR |
| PUT | `/v1/pessoas/{id}` | ADMINISTRADOR, COORDENADOR |
| DELETE | `/v1/pessoas/{id}` | ADMINISTRADOR |

### 2. TipoPessoa — `/v1/tipos-pessoa`
```typescript
interface TipoPessoaDTO {
  idTipoPessoa?: number;
  nome: string; // obrigatório, max 255 — ex: "Aluno", "Professor", "Responsável"
}
```
Todos os endpoints exigem **apenas ADMINISTRADOR** (sem COORDENADOR).

### 3. Contato — `/v1/contatos`
```typescript
interface ContatoDTO {
  idContato?: number;
  tipoContato: string; // obrigatório, max 255 — ex: "Celular", "E-mail", "WhatsApp"
  contato: string;     // obrigatório, max 255 — o valor em si (ex: "(12) 99999-0000")
}
```
Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

### 4. ContatoPessoa — `/v1/contatos-pessoa`
```typescript
interface ContatoPessoaDTO {
  idContatoPessoa?: number;
  idPessoa: number;  // obrigatório — FK para Pessoa
  idContato: number; // obrigatório — FK para Contato
  nome?: string;     // apelido da associação, ex: "Celular pessoal" (max 255)
}
```
> ⚠️ **UPDATE só altera `nome`** — não é possível trocar o `idPessoa` ou `idContato` de uma associação existente. Para vincular um contato diferente, deve-se deletar a associação e criar uma nova.

Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

### 5. Endereco — `/v1/enderecos`
```typescript
interface EnderecoDTO {
  idEndereco?: number;
  uf?: string;          // max 2 — ex: "SP"
  cidade?: string;      // max 255
  bairro?: string;      // max 255
  rua?: string;         // max 255
  numero?: string;      // max 10
  cep?: string;         // max 9 — ex: "12300-000"
  complemento?: string; // max 255
}
```
Todos os campos são opcionais no backend. Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

### 6. EnderecoPessoa — `/v1/enderecos-pessoa`
```typescript
interface EnderecoPessoaDTO {
  idEnderecoPessoa?: number;
  idPessoa: number;   // obrigatório — FK para Pessoa
  idEndereco: number; // obrigatório — FK para Endereço
  nome?: string;      // apelido, ex: "Residencial" (max 255)
}
```
> ⚠️ **UPDATE só altera `nome`** — mesma restrição do ContatoPessoa.

Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

### 7. PessoaResponsavel — `/v1/pessoas-responsavel`
```typescript
interface PessoaResponsavelDTO {
  idPessoaResponsavel?: number;
  idAluno: number;       // obrigatório — FK para Pessoa (o aluno)
  idResponsavel: number; // obrigatório — FK para Pessoa (o responsável)
  parentesco?: string;   // opcional — ex: "Mãe", "Pai", "Avó" (max 255)
}
```
> ⚠️ **UPDATE só altera `parentesco`** — não é possível trocar o aluno ou responsável de um vínculo existente.
> Ambos `idAluno` e `idResponsavel` são IDs de Pessoa — o mesmo endpoint `/v1/pessoas` é usado para buscar os dois.

Leitura/Criação/Atualização: ADMINISTRADOR, COORDENADOR. Deletar: ADMINISTRADOR.

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/pessoa/
├── types.ts              ← 7 DTOs TypeScript
├── pessoaService.ts      ← funções Axios para todos os 7 recursos
└── pessoaQueries.ts      ← hooks TanStack Query
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── pessoas/
│   ├── page.tsx              ← Lista de pessoas com filtro por tipo
│   └── [id]/
│       └── page.tsx          ← Detalhe: dados + contatos + endereços + responsáveis
│
└── admin/
    └── pessoa/
        └── page.tsx          ← Tabs: TipoPessoa | Contatos (globais) | Endereços (globais)
```

---

## Tela 1 — Lista de Pessoas (`/pessoas`)

**Tipo:** Server Component passando dados para Client Component.

**Conteúdo:**
- `PageHeader` com título "Pessoas" e botão **+ Nova Pessoa** (ADMINISTRADOR, COORDENADOR)
- `Table` com colunas: **ID** | **Nome** | **Tipo** | **Situação** | **Ações**
  - Coluna "Tipo": exibir o `nome` do TipoPessoa (fazer lookup local após buscar ambas as listas)
  - Coluna "Situação": exibir `Badge` — `default` para "Ativo", `secondary` para "Inativo"
- Filtro por tipo: `Select` acima da tabela com opções vindas de `GET /v1/tipos-pessoa` (filtro client-side)
- Ações por linha: botão **Ver detalhes** (→ `/pessoas/[id]`) + ícone editar + ícone excluir (ADMINISTRADOR)
- Dialog de criar/editar com todos os campos de `PessoaDTO`
- `AlertDialog` de confirmação antes de excluir

**Dialog de criar/editar Pessoa:**
- `Select` de TipoPessoa — obrigatório, lookup `GET /v1/tipos-pessoa`
- `Input` de Nome — obrigatório
- `Select` de Sexo — opções fixas: "Masculino", "Feminino", "Outro"
- `Input` type date de Data de Nascimento — enviar como "YYYY-MM-DD"
- `Select` de Situação — opções fixas: "Ativo", "Inativo"
- `Textarea` de Observação — opcional

---

## Tela 2 — Detalhe da Pessoa (`/pessoas/[id]`)

Esta é a tela central do domínio. Organizada em 4 seções via `Card` (shadcn/ui):

### Card 1 — Dados da Pessoa
- Exibir todos os campos do `PessoaDTO` (Nome, Tipo, Sexo, Nascimento, Situação, Obs)
- Botão **Editar** (ADMINISTRADOR, COORDENADOR) abre Dialog pré-preenchido
- Botão **← Voltar**

### Card 2 — Contatos Vinculados
**Carregamento:** `GET /v1/contatos-pessoa` → filtrar client-side por `idPessoa === id`

**Tabela:** colunas **Apelido (`nome`)** | **Tipo** | **Valor** | **Ações**
> "Tipo" e "Valor" vêm do Contato associado — é necessário buscar o Contato via `GET /v1/contatos/{idContato}` para cada linha, ou buscar todos os contatos e fazer o join localmente.

**Botão "+ Vincular Contato"** (ADMINISTRADOR, COORDENADOR) — abre Dialog com:
- `Select` de Contato existente — lookup `GET /v1/contatos`, exibir `tipoContato + ": " + contato`
- `Input` de Apelido (`nome`) — opcional
- Salvar: `POST /v1/contatos-pessoa` com `{ idPessoa: id, idContato, nome }`

**Ação de editar apelido** — abre Dialog com apenas o campo `nome` (o único campo editável via PUT)

**Ação de desvincular** — `AlertDialog` + `DELETE /v1/contatos-pessoa/{idContatoPessoa}`

### Card 3 — Endereços Vinculados
**Carregamento:** `GET /v1/enderecos-pessoa` → filtrar client-side por `idPessoa === id`

**Tabela:** colunas **Apelido (`nome`)** | **CEP** | **Rua** | **Número** | **Cidade/UF** | **Ações**
> Dados de CEP/Rua/etc. vêm do Endereço associado — fazer join local com `GET /v1/enderecos`.

**Botão "+ Vincular Endereço"** (ADMINISTRADOR, COORDENADOR) — abre Dialog com:
- `Select` de Endereço existente — lookup `GET /v1/enderecos`, exibir `rua, numero - cidade/UF` ou CEP
- `Input` de Apelido (`nome`) — opcional, ex: "Residencial"
- Salvar: `POST /v1/enderecos-pessoa` com `{ idPessoa: id, idEndereco, nome }`

**Ação de editar apelido** — Dialog só com o campo `nome`
**Ação de desvincular** — `AlertDialog` + `DELETE /v1/enderecos-pessoa/{idEnderecoPessoa}`

### Card 4 — Responsáveis (exibir sempre, relevante para alunos)
**Carregamento:** `GET /v1/pessoas-responsavel` → filtrar client-side por `idAluno === id`

**Tabela:** colunas **ID Responsável** | **Nome Responsável** | **Parentesco** | **Ações**
> Buscar o nome do responsável via `GET /v1/pessoas/{idResponsavel}` ou join local.

**Botão "+ Vincular Responsável"** (ADMINISTRADOR, COORDENADOR) — abre Dialog com:
- `Select` de Pessoa (responsável) — lookup `GET /v1/pessoas`, exibir nome, value = idPessoa
- `Input` de Parentesco — opcional, ex: "Mãe"
- Salvar: `POST /v1/pessoas-responsavel` com `{ idAluno: id, idResponsavel, parentesco }`

**Ação de editar parentesco** — Dialog só com o campo `parentesco`
**Ação de desvincular** — `AlertDialog` + `DELETE /v1/pessoas-responsavel/{idPessoaResponsavel}`

---

## Tela 3 — Configurações Administrativas (`/admin/pessoa`)

Página única com **Tabs** (shadcn/ui) em 3 abas:

### Aba 1 — Tipos de Pessoa (`/v1/tipos-pessoa`)
> ⚠️ Esta aba é visível e editável apenas por **ADMINISTRADOR** (sem COORDENADOR).

- `Table`: **ID** | **Nome** | **Ações**
- Dialog: campo `nome` obrigatório
- `AlertDialog` exclusão

### Aba 2 — Contatos (`/v1/contatos`)
Gerenciamento do cadastro global de contatos (telefones, e-mails).

- `Table`: **ID** | **Tipo** | **Valor** | **Ações**
- Dialog criar/editar:
  - `Select` de Tipo — opções sugeridas: "Celular", "Telefone Fixo", "E-mail", "WhatsApp" + campo livre para outros
  - `Input` de Valor — obrigatório
- `AlertDialog` exclusão

### Aba 3 — Endereços (`/v1/enderecos`)
Gerenciamento do cadastro global de endereços.

- `Table`: **ID** | **CEP** | **Rua** | **Número** | **Cidade** | **UF** | **Ações**
- Dialog criar/editar com todos os campos do `EnderecoDTO`:
  - `Input` CEP (max 9, ex: "12300-000")
  - `Input` UF (max 2, ex: "SP")
  - `Input` Cidade
  - `Input` Bairro
  - `Input` Rua
  - `Input` Número (max 10)
  - `Input` Complemento
- `AlertDialog` exclusão

---

## Controle de acesso

```typescript
// Guard SSR no layout /admin/
// src/app/(dashboard)/admin/layout.tsx — já criado no domínio Turma (reutilizar)

// Em componentes — exemplo de múltiplas granularidades
const { hasAny, is } = useRoles();

// Botão de nova Pessoa — ADMINISTRADOR e COORDENADOR
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button>+ Nova Pessoa</Button>}

// Botão de excluir — apenas ADMINISTRADOR
{is('ADMINISTRADOR') && <Button variant="ghost">Excluir</Button>}

// Aba TipoPessoa — apenas ADMINISTRADOR pode ver e editar
// Implementar como verificação no próprio componente da aba, não apenas escondendo o botão
{is('ADMINISTRADOR') ? <TipoPessoaTab /> : (
  <p className="text-muted-foreground">Acesso restrito ao Administrador.</p>
)}
```

---

## Validação Zod por recurso

```typescript
// Pessoa
const schemaPessoa = z.object({
  idTipoPessoa: z.coerce.number({ required_error: 'Tipo é obrigatório' }),
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
  sexo: z.enum(['Masculino', 'Feminino', 'Outro']).optional(),
  dataNascimento: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Use o formato YYYY-MM-DD').optional().or(z.literal('')),
  situacao: z.enum(['Ativo', 'Inativo']).optional(),
  obs: z.string().max(255).optional(),
});

// TipoPessoa / Contato (campo simples)
const schemaNome = z.object({ nome: z.string().min(1).max(255) });

// Contato (dois campos obrigatórios)
const schemaContato = z.object({
  tipoContato: z.string().min(1, 'Tipo é obrigatório').max(255),
  contato: z.string().min(1, 'Valor é obrigatório').max(255),
});

// ContatoPessoa (vincular)
const schemaContatoPessoa = z.object({
  idContato: z.coerce.number({ required_error: 'Contato é obrigatório' }),
  nome: z.string().max(255).optional(),
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
```

---

## Padrão de service

```typescript
// src/features/pessoa/pessoaService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { PessoaDTO, ContatoPessoaDTO, EnderecoPessoaDTO, PessoaResponsavelDTO } from './types';

export async function listarPessoas(): Promise<PessoaDTO[]> {
  const res = await api.get<ApiResponse<PessoaDTO[]>>('/v1/pessoas');
  return res.data.data ?? [];
}

export async function buscarPessoa(id: number): Promise<PessoaDTO> {
  const res = await api.get<ApiResponse<PessoaDTO>>(`/v1/pessoas/${id}`);
  return res.data.data!;
}

export async function criarPessoa(dto: Omit<PessoaDTO, 'idPessoa'>): Promise<PessoaDTO> {
  const res = await api.post<ApiResponse<PessoaDTO>>('/v1/pessoas', dto);
  return res.data.data!;
}

// Vincular contato
export async function vincularContato(
  dto: { idPessoa: number; idContato: number; nome?: string }
): Promise<ContatoPessoaDTO> {
  try {
    const res = await api.post<ApiResponse<ContatoPessoaDTO>>('/v1/contatos-pessoa', dto);
    return res.data.data!;
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao vincular contato');
  }
}

export async function atualizarApelido ContatoPessoa(
  id: number,
  nome: string
): Promise<ContatoPessoaDTO> {
  const res = await api.put<ApiResponse<ContatoPessoaDTO>>(
    `/v1/contatos-pessoa/${id}`,
    { nome }
  );
  return res.data.data!;
}

// Repetir padrão para enderecos-pessoa, pessoas-responsavel, contatos, enderecos, tipos-pessoa
```

> Corrigir o nome da função acima para `atualizarApelidoContatoPessoa` — o exemplo acima tem um espaço acidental no nome.

---

## Query keys (usar consistentemente)

```typescript
['pessoas']                      // lista completa
['pessoa', id]                   // detalhe de uma pessoa
['tipos-pessoa']                 // lookup para Select
['contatos']                     // cadastro global de contatos
['contatos-pessoa']              // todas as associações
['contatos-pessoa', idPessoa]    // associações de uma pessoa específica (filtro client-side)
['enderecos']                    // cadastro global de endereços
['enderecos-pessoa']             // todas as associações
['enderecos-pessoa', idPessoa]   // associações de uma pessoa específica
['pessoas-responsavel']          // todos os vínculos
['pessoas-responsavel', idAluno] // vínculos de um aluno específico
```

---

## Considerações importantes de UX

### Modelo normalizado: Contato e Endereço são entidades próprias

O backend usa normalização — um mesmo número de telefone pode estar vinculado a várias pessoas. O fluxo correto é:

1. Criar o Contato em `/v1/contatos` (ou reutilizar um existente)
2. Vincular via `/v1/contatos-pessoa`

Para simplificar o uso, o Dialog de "Vincular Contato" pode oferecer duas opções via `Tabs` internas:
- **Aba "Contato existente"** — Select dos contatos já cadastrados
- **Aba "Novo contato"** — formulário inline que cria o Contato e já vincula em sequência

### PUT em ContatoPessoa e EnderecoPessoa — apenas `nome`

O campo `nome` é um apelido da associação (ex: "Residencial", "Celular pessoal"). Os IDs não podem ser alterados via PUT. Comunicar isso visualmente — o ícone de editar na linha deve abrir um Dialog com apenas o campo `nome`, não todos os campos da associação.

### PessoaResponsavel — `idAluno` e `idResponsavel` são ambos Pessoas

O Select de responsável no Dialog de vincular deve listar **todas as pessoas** (`GET /v1/pessoas`). O filtro de quem é "aluno" e quem é "responsável" é semântico — o backend não filtra por tipo. Exibir o `nome` de cada pessoa no Select.

### Campo `dataNascimento` — string no backend

O backend armazena como `String`, não como `Date`. Usar `<Input type="date">` no frontend e formatar como `"YYYY-MM-DD"` ao enviar. Ao exibir, formatar para `"DD/MM/YYYY"` com `new Date(...).toLocaleDateString('pt-BR')`.

---

## O que NÃO fazer

- Não tentar editar `idContato` ou `idEndereco` via PUT em ContatoPessoa/EnderecoPessoa — o endpoint só aceita `nome`
- Não tentar editar `idAluno` ou `idResponsavel` via PUT em PessoaResponsavel — o endpoint só aceita `parentesco`
- Não criar um Select de Pessoa que carregue todos sem paginação em ambientes com muitos registros — para MVP, listar todos e filtrar client-side é aceitável
- Não expor a aba TipoPessoa para COORDENADOR — apenas ADMINISTRADOR tem acesso a esse endpoint

---

## Checklist de entrega

- [ ] `src/features/pessoa/types.ts` — 7 DTOs TypeScript
- [ ] `src/features/pessoa/pessoaService.ts` — funções de API para todos os 7 recursos
- [ ] `src/features/pessoa/pessoaQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `src/app/(dashboard)/pessoas/page.tsx` — lista com filtro por tipo + Dialog criar/editar
- [ ] `src/app/(dashboard)/pessoas/[id]/page.tsx` — 4 Cards: Dados | Contatos | Endereços | Responsáveis
- [ ] Card Contatos: vincular (Select existente), editar apelido, desvincular
- [ ] Card Endereços: vincular (Select existente), editar apelido, desvincular
- [ ] Card Responsáveis: vincular (Select de Pessoa), editar parentesco, desvincular
- [ ] `src/app/(dashboard)/admin/pessoa/page.tsx` — Tabs: TipoPessoa | Contatos | Endereços
- [ ] Aba TipoPessoa bloqueada para COORDENADOR (mensagem de acesso restrito)
- [ ] `dataNascimento` exibido como DD/MM/YYYY, enviado como YYYY-MM-DD
- [ ] Loading Skeleton em todas as listas
- [ ] Toast de erro com campo `error` da ApiResponse
- [ ] AlertDialog com texto claro antes de desvincular qualquer associação
