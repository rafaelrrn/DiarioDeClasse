# Prompt para o Claude Code — Frontend: CRUD do domínio Turma

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Turma** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Contexto do domínio

O domínio `turma` tem 5 recursos. Eles se dividem em dois grupos por finalidade:

**Operacional (usado no dia a dia):**
- **Turma** — a sala de aula. Professor e Coordenador acessam.
- **AlunoTurma** — matrícula de aluno em turma. Com soft delete (histórico preservado).

**Configuração administrativa:**
- **Disciplina** — cadastro simples de disciplinas
- **ComponenteCurricular** — cadastro de componentes (ex: "Língua Portuguesa")
- **Classe** — vínculo entre Turma + Professor + Curso + Turno + Instituição + ComponenteCurricular

---

## Endpoints e DTOs

### 1. Turma — `/v1/turmas`
```typescript
interface TurmaDTO {
  idTurma?: number;
  nome: string; // obrigatório, max 255
}
```

Endpoints:
- `GET    /v1/turmas`        → `ApiResponse<TurmaDTO[]>`   — ADMINISTRADOR, COORDENADOR
- `GET    /v1/turmas/{id}`   → `ApiResponse<TurmaDTO>`     — ADMINISTRADOR, COORDENADOR
- `POST   /v1/turmas`        → `ApiResponse<TurmaDTO>` 201 — ADMINISTRADOR
- `PUT    /v1/turmas/{id}`   → `ApiResponse<TurmaDTO>`     — ADMINISTRADOR
- `DELETE /v1/turmas/{id}`   → `ApiResponse<null>`         — ADMINISTRADOR

**Endpoints especiais de matrícula (aninhados em Turma):**
- `GET  /v1/turmas/{id}/alunos` → `ApiResponse<AlunoTurmaDTO[]>` — ADMINISTRADOR, COORDENADOR, PROFESSOR
- `POST /v1/turmas/{id}/alunos` → `ApiResponse<AlunoTurmaDTO>` 201 — ADMINISTRADOR, COORDENADOR
  - HTTP 422 se aluno já tiver matrícula ativa na mesma turma

### 2. AlunoTurma — `/v1/alunos-turma`
```typescript
interface AlunoTurmaDTO {
  idAlunoTurma?: number;
  idAluno: number;  // obrigatório — FK para Pessoa (aluno)
  idTurma: number;  // obrigatório — FK para Turma
  obs?: string;     // max 255, observação opcional
}
```

Endpoints:
- `GET    /v1/alunos-turma`      → `ApiResponse<AlunoTurmaDTO[]>` — ADMINISTRADOR, COORDENADOR, PROFESSOR
- `GET    /v1/alunos-turma/{id}` → `ApiResponse<AlunoTurmaDTO>`   — ADMINISTRADOR, COORDENADOR, PROFESSOR
- `POST   /v1/alunos-turma`      → `ApiResponse<AlunoTurmaDTO>` 201 — ADMINISTRADOR, COORDENADOR
- `DELETE /v1/alunos-turma/{id}` → `ApiResponse<null>` + message "Matrícula desativada com sucesso"

> ⚠️ **Soft delete**: o DELETE **não remove** o registro — define `ativo=false`. O histórico é preservado para fins pedagógicos. Após desativar, o aluno pode ser rematriculado na mesma turma. **NÃO existe endpoint PUT para AlunoTurma** — não é possível editar uma matrícula, só desativar e criar nova.

### 3. Disciplina — `/v1/disciplinas`
```typescript
interface DisciplinaDTO {
  idDisciplina?: number;
  nome: string; // obrigatório, max 255
}
```

CRUD completo padrão. Leitura: ADMINISTRADOR, COORDENADOR. Escrita: ADMINISTRADOR.

### 4. ComponenteCurricular — `/v1/componentes-curriculares`
```typescript
interface ComponenteCurricularDTO {
  idComponenteCurricular?: number;
  nome: string;  // obrigatório, max 255
  obs?: string;  // texto livre, opcional
}
```

CRUD completo padrão. Leitura: ADMINISTRADOR, COORDENADOR. Escrita: ADMINISTRADOR.

### 5. Classe — `/v1/classes`
```typescript
interface ClasseDTO {
  idClasse?: number;
  idInstituicaoEnsino: number;        // obrigatório — FK → InstituicaoEnsino
  idComponenteCurricular?: number;    // opcional    — FK → ComponenteCurricular
  idCurso: number;                    // obrigatório — FK → Curso
  idTurno: number;                    // obrigatório — FK → Turno
  idTurma: number;                    // obrigatório — FK → Turma
  idProfessor: number;                // obrigatório — FK → Pessoa (professor)
}
```

CRUD completo padrão. Leitura: ADMINISTRADOR, COORDENADOR. Escrita: ADMINISTRADOR.

> As FKs de `Classe` buscam recursos de outros domínios:
> - `idInstituicaoEnsino` → `GET /v1/instituicoes-ensino`
> - `idComponenteCurricular` → `GET /v1/componentes-curriculares`
> - `idCurso` → `GET /v1/cursos`
> - `idTurno` → `GET /v1/turnos`
> - `idTurma` → `GET /v1/turmas`
> - `idProfessor` → `GET /v1/pessoas` (filtrar quem for necessário — o backend retorna todas as pessoas)

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/turma/
├── types.ts               ← 5 DTOs TypeScript
├── turmaService.ts        ← funções Axios para todos os 5 recursos
└── turmaQueries.ts        ← hooks TanStack Query (useQuery + useMutation)
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── turmas/
│   ├── page.tsx                ← Lista de turmas (Server Component)
│   └── [id]/
│       └── page.tsx            ← Detalhe da turma + gerenciar alunos matriculados
│
└── admin/
    └── turma/
        └── page.tsx            ← Tabs: Disciplina | ComponenteCurricular | Classe
```

---

## Tela 1 — Lista de Turmas (`/turmas`)

**Tipo:** Server Component que passa dados para um Client Component interativo.

**Conteúdo:**
- `PageHeader` com título "Turmas" e botão **+ Nova Turma** (visível apenas para ADMINISTRADOR)
- `Table` (shadcn/ui) com colunas: **ID** | **Nome** | **Ações**
- Coluna Ações: ícone de editar + ícone de excluir (visíveis apenas para ADMINISTRADOR) + botão **Ver alunos** (abre `/turmas/[id]`)
- Dialog de criar/editar com campo `nome` obrigatório (validação Zod)
- `AlertDialog` de confirmação antes de excluir

---

## Tela 2 — Detalhe da Turma e Matrículas (`/turmas/[id]`)

Esta é a tela central do fluxo operacional. Deve conter duas seções:

### Seção 1 — Dados da Turma
- Cabeçalho com nome da turma, botão **Editar** (ADMINISTRADOR apenas)
- Botão **← Voltar para Turmas**

### Seção 2 — Alunos Matriculados

**Carregamento:** `GET /v1/turmas/{id}/alunos`

**Tabela** com colunas: **ID Matrícula** | **ID Aluno** | **Observação** | **Ações**

> Nota: o backend retorna apenas o `idAluno` (não o nome). Exibir o ID por enquanto — em versão futura, será cruzado com o módulo de pessoas.

**Botão "+ Matricular Aluno"** (ADMINISTRADOR e COORDENADOR) abre um Dialog com:
- Campo `idAluno` — Input numérico obrigatório (label: "ID do Aluno")
- Campo `obs` — `Textarea`, opcional, max 255, label: "Observação"
- Ao salvar: `POST /v1/turmas/{id}/alunos`
- HTTP 422 do backend → exibir mensagem via toast: ex. "Aluno já possui matrícula ativa nesta turma"

**Botão de desativar matrícula** (ícone, ADMINISTRADOR e COORDENADOR):
- Abre `AlertDialog` com mensagem: "Desativar esta matrícula? O histórico será preservado e o aluno poderá ser rematriculado posteriormente."
- Ao confirmar: `DELETE /v1/alunos-turma/{idAlunoTurma}`
- Toast: "Matrícula desativada com sucesso"
- Invalidar query `['turma', id, 'alunos']` após sucesso

> ⚠️ Usar o texto "Desativar" (não "Excluir") — o soft delete preserva o histórico. O usuário precisa entender que o registro continua existindo.

---

## Tela 3 — Configurações Administrativas (`/admin/turma`)

Página única com **Tabs** (shadcn/ui) em 3 abas:

### Aba 1 — Disciplinas

- `Table` com colunas: **ID** | **Nome** | **Ações**
- Dialog criar/editar: campo `nome` obrigatório
- `AlertDialog` exclusão

### Aba 2 — Componentes Curriculares

- `Table` com colunas: **ID** | **Nome** | **Observação** | **Ações**
- Dialog criar/editar:
  - `nome` — Input obrigatório
  - `obs` — `Textarea` opcional

### Aba 3 — Classes

- `Table` com colunas: **ID** | **Instituição** | **Turma** | **Curso** | **Turno** | **Professor (ID)** | **Componente** | **Ações**
- Exibir os **nomes** resolvidos onde possível (buscar os lookups em paralelo)
- Dialog criar/editar com 6 Selects:
  - **Instituição** (obrigatório) — `GET /v1/instituicoes-ensino`
  - **Turma** (obrigatório) — `GET /v1/turmas`
  - **Curso** (obrigatório) — `GET /v1/cursos`
  - **Turno** (obrigatório) — `GET /v1/turnos`
  - **Professor** (obrigatório) — `GET /v1/pessoas`, exibir nome, value = `idPessoa`
  - **Componente Curricular** (opcional) — `GET /v1/componentes-curriculares` + opção "Nenhum"
- Todos os 6 lookups carregados em paralelo (`useQueries` ou `Promise.all`)

---

## Controle de acesso

```typescript
// Guard SSR no layout /admin/
// src/app/(dashboard)/admin/layout.tsx
import { redirect } from 'next/navigation';
import { buscarMeServer } from '@/features/auth/authService';

export default async function AdminLayout({ children }) {
  const user = await buscarMeServer();
  if (user?.role !== 'ADMINISTRADOR') redirect('/acesso-negado');
  return <>{children}</>;
}
```

```typescript
// Em componentes interativos — ocultar botões de escrita para COORDENADOR e PROFESSOR
const { hasAny } = useRoles();

// Somente ADMINISTRADOR cria/edita/exclui turmas
{hasAny('ADMINISTRADOR') && <Button>+ Nova Turma</Button>}

// ADMINISTRADOR e COORDENADOR podem matricular/desativar alunos
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button>+ Matricular Aluno</Button>}
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button variant="ghost">Desativar</Button>}
```

---

## Validação Zod por recurso

```typescript
// Turma / Disciplina
const schemaNome = z.object({
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
});

// ComponenteCurricular
const schemaComponente = z.object({
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
  obs: z.string().max(500).optional(),
});

// AlunoTurma (matricular)
const schemaMatricula = z.object({
  idAluno: z.coerce.number({ required_error: 'ID do aluno é obrigatório' }).positive(),
  obs: z.string().max(255).optional(),
});

// Classe
const schemaClasse = z.object({
  idInstituicaoEnsino:     z.coerce.number({ required_error: 'Instituição é obrigatória' }),
  idTurma:                 z.coerce.number({ required_error: 'Turma é obrigatória' }),
  idCurso:                 z.coerce.number({ required_error: 'Curso é obrigatório' }),
  idTurno:                 z.coerce.number({ required_error: 'Turno é obrigatório' }),
  idProfessor:             z.coerce.number({ required_error: 'Professor é obrigatório' }),
  idComponenteCurricular:  z.coerce.number().optional().nullable(),
});
```

---

## Padrão de service

```typescript
// src/features/turma/turmaService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { TurmaDTO, AlunoTurmaDTO } from './types';

// --- Turma ---
export async function listarTurmas(): Promise<TurmaDTO[]> {
  const res = await api.get<ApiResponse<TurmaDTO[]>>('/v1/turmas');
  return res.data.data ?? [];
}

export async function listarAlunosDaTurma(idTurma: number): Promise<AlunoTurmaDTO[]> {
  const res = await api.get<ApiResponse<AlunoTurmaDTO[]>>(`/v1/turmas/${idTurma}/alunos`);
  return res.data.data ?? [];
}

export async function matricularAluno(
  idTurma: number,
  payload: { idAluno: number; obs?: string }
): Promise<AlunoTurmaDTO> {
  try {
    const res = await api.post<ApiResponse<AlunoTurmaDTO>>(
      `/v1/turmas/${idTurma}/alunos`,
      { ...payload, idTurma }
    );
    return res.data.data!;
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao matricular aluno');
  }
}

export async function desativarMatricula(id: number): Promise<void> {
  try {
    await api.delete(`/v1/alunos-turma/${id}`);
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao desativar matrícula');
  }
}

// Repetir padrão para criar/atualizar/deletar Turma, Disciplina, ComponenteCurricular, Classe
```

---

## Padrão de query keys (usar consistentemente)

```typescript
// Chaves de cache TanStack Query — manter estas exatas para invalidação correta
['turmas']                          // lista de turmas
['turma', id]                       // detalhe de uma turma
['turma', id, 'alunos']             // alunos de uma turma específica
['alunos-turma']                    // todas as matrículas
['disciplinas']
['componentes-curriculares']
['classes']
// Lookups externos (usados no Dialog de Classe):
['instituicoes-ensino']             // já existem do domínio instituicao
['cursos']
['turnos']
['pessoas']                         // para Select de professor
```

---

## Estados de UI obrigatórios

| Situação | Componente |
|----------|-----------|
| Carregando lista | `Skeleton` em lugar da Table |
| Erro ao carregar | Mensagem inline + botão "Tentar novamente" |
| Lista vazia | Mensagem "Nenhum registro encontrado" dentro da Table |
| Submit em andamento | `disabled={isPending}` no botão + texto "Salvando..." |
| Matrícula duplicada (422) | Toast destrutivo com mensagem do backend |
| Sucesso de criar/editar | Fechar Dialog + toast de sucesso |
| Sucesso de desativar | Fechar AlertDialog + toast "Matrícula desativada" |

---

## O que NÃO fazer

- Não chamar `DELETE /v1/alunos-turma/{id}` e exibir "Excluído" — o texto correto é **"Desativado"**
- Não criar tela de edição de AlunoTurma — o endpoint PUT não existe no backend
- Não confundir os dois endpoints de matricular:
  - `POST /v1/turmas/{id}/alunos` → preferido (contexto da turma, usado na tela de detalhe)
  - `POST /v1/alunos-turma` → alternativo (mesmo comportamento, evitar duplicar)
- Não criar rotas separadas para Disciplina e ComponenteCurricular — tudo em Tabs em `/admin/turma`
- Não esquecer que `idProfessor` em `ClasseDTO` é uma `Pessoa` — buscar em `/v1/pessoas`
- Não criar componentes de UI do zero — usar exclusivamente shadcn/ui

---

## Checklist de entrega

- [ ] `src/features/turma/types.ts` — 5 DTOs TypeScript (TurmaDTO, AlunoTurmaDTO, DisciplinaDTO, ComponenteCurricularDTO, ClasseDTO)
- [ ] `src/features/turma/turmaService.ts` — funções de API para todos os recursos
- [ ] `src/features/turma/turmaQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `src/app/(dashboard)/turmas/page.tsx` — lista de turmas com CRUD
- [ ] `src/app/(dashboard)/turmas/[id]/page.tsx` — detalhe da turma + lista de alunos + matricular + desativar
- [ ] `src/app/(dashboard)/admin/turma/page.tsx` — Tabs: Disciplina | ComponenteCurricular | Classe
- [ ] Dialog de Classe com 6 Selects e lookups em paralelo
- [ ] Texto "Desativar" (não "Excluir") no AlertDialog de matrícula
- [ ] AlertDialog com mensagem explicativa sobre soft delete
- [ ] Botões de escrita ocultados por role (ADMINISTRADOR vs COORDENADOR vs PROFESSOR)
- [ ] Loading com Skeleton em todas as listas
- [ ] Toast de erro com mensagem do campo `error` da ApiResponse (especialmente HTTP 422)
