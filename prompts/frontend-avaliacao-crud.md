# Prompt para o Claude Code — Frontend: CRUD do domínio Avaliação

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Avaliação** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. O componente `SituacaoBadge` e os DTOs `MediaDisciplinaDTO` e `BoletimResponseDTO` já estão documentados no INSTRUCTIONS_FRONTEND.md (seção 6.6 e 9.2) — use como referência. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Análise do backend — lógica já implementada

O domínio `avaliacao` tem **3 controllers e 1 service principal** com lógica de negócio substancial. Toda a matemática de média ponderada, situação do aluno e geração de boletim já está implementada no backend — o frontend **não recalcula nada**, apenas exibe o que o backend retorna.

### Controllers e seus papéis

| Controller | Base URL | Papel |
|-----------|---------|-------|
| `AvaliacaoController` | `/v1/avaliacoes` | CRUD de avaliações + lançamento em lote de notas |
| `AlunoAvaliacaoController` | `/v1/alunos-avaliacao` | CRUD de registros individuais de nota |
| `AlunoNotasController` | `/v1/alunos/{id}` | Consulta de notas e boletim por aluno |

### Endpoints completos

**AvaliacaoController — `/v1/avaliacoes`:**

| Método | Rota | Roles | Descrição |
|--------|------|-------|-----------|
| GET | `/v1/avaliacoes` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Lista avaliações ativas |
| GET | `/v1/avaliacoes/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Busca por ID |
| POST | `/v1/avaliacoes` | ADMINISTRADOR, COORDENADOR, **PROFESSOR** | Cria avaliação |
| PUT | `/v1/avaliacoes/{id}` | ADMINISTRADOR, COORDENADOR, **PROFESSOR** | Atualiza avaliação |
| DELETE | `/v1/avaliacoes/{id}` | ADMINISTRADOR, **COORDENADOR** | Soft delete da avaliação |
| POST | `/v1/avaliacoes/{id}/notas` | ADMINISTRADOR, **PROFESSOR** | **Lança notas em lote** ← endpoint principal |

**AlunoAvaliacaoController — `/v1/alunos-avaliacao`:**

| Método | Rota | Roles | Descrição |
|--------|------|-------|-----------|
| GET | `/v1/alunos-avaliacao` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Lista todas as notas ativas |
| GET | `/v1/alunos-avaliacao/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Busca por ID |
| POST | `/v1/alunos-avaliacao` | ADMINISTRADOR, PROFESSOR | Lança nota individual |
| PUT | `/v1/alunos-avaliacao/{id}` | ADMINISTRADOR, PROFESSOR | Corrige nota e obs |
| DELETE | `/v1/alunos-avaliacao/{id}` | **ADMINISTRADOR apenas** | Soft delete da nota |

**AlunoNotasController — `/v1/alunos/{id}`:**

| Método | Rota | Roles | Descrição |
|--------|------|-------|-----------|
| GET | `/v1/alunos/{id}/notas` | **Todos os roles** (incl. ALUNO e RESPONSAVEL) | Notas ativas do aluno |
| GET | `/v1/alunos/{id}/boletim` | **Todos os roles** | Boletim completo com médias e frequência |

---

## DTOs

```typescript
// Avaliação
interface AvaliacaoDTO {
  idAvaliacao?: number;
  idDisciplina: number;         // obrigatório — FK para Disciplina
  idCalendarioEscolar?: number; // opcional — FK para CalendarioEscolar
  materia?: string;             // max 255 — conteúdo/assunto (ex: "Frações Decimais")
  dia?: string;                 // max 255 — data como string (ex: "2025-06-15")
  peso?: number;                // inteiro — peso para média ponderada (ex: 7=prova, 3=trabalho)
}

// Registro individual de nota
interface AlunoAvaliacaoDTO {
  idAlunoAvaliacao?: number;
  idAluno: number;     // obrigatório
  idAvaliacao: number; // obrigatório
  nota?: number;       // 0.0 a 10.0 (Float)
  obs?: string;        // max 255
}

// Entrada do lançamento em lote — array enviado para POST /v1/avaliacoes/{id}/notas
interface NotaLancamentoDTO {
  idAluno: number; // obrigatório
  nota: number;    // obrigatório, 0.0 a 10.0
  obs?: string;    // opcional
}

// Resultado do boletim
type SituacaoAluno = 'APROVADO' | 'EM_RECUPERACAO' | 'REPROVADO_NOTA' | 'REPROVADO_FREQUENCIA';

interface MediaDisciplinaDTO {
  idDisciplina: number;
  nomeDisciplina: string;
  mediaCalculada: number;   // 0.0 a 10.0, já calculado pelo backend
  situacao: SituacaoAluno;  // já calculado pelo backend
}

interface BoletimResponseDTO {
  idAluno: number;
  nomeAluno: string;
  frequencia: FrequenciaResumoDTO; // mesmo DTO do domínio frequência
  disciplinas: MediaDisciplinaDTO[];
}
```

---

## Regras de negócio implementadas no backend (crítico para o frontend)

### 1. Peso da avaliação — média ponderada

O campo `peso` em `AvaliacaoDTO` define a relevância de cada avaliação no cálculo da média:

```
média = Σ(nota × peso) / Σ(peso)
```

- `peso = null` → o backend assume peso **1** automaticamente
- Exemplos típicos: Prova = 7, Trabalho = 3, Participação = 1
- O cálculo é 100% feito no backend — o frontend apenas exibe `mediaCalculada` do boletim

### 2. SituacaoAluno — 4 estados com precedência estrita

```
1. REPROVADO_FREQUENCIA → frequência < 75% (LDB Art. 24) — prevalece sobre a nota
2. APROVADO            → média ≥ 5.0 (e freq ≥ 75%)
3. EM_RECUPERACAO      → média 3.0 a 4.9
4. REPROVADO_NOTA      → média < 3.0 (reprovação direta, sem direito a recuperação)
```

> O componente `SituacaoBadge` já está definido no INSTRUCTIONS_FRONTEND.md — reutilizá-lo.

### 3. Lançamento em lote — TRANSACIONAL (comportamento crítico diferente da frequência)

```
POST /v1/avaliacoes/{id}/notas
Body: [ { idAluno, nota, obs? }, ... ]
```

> ⚠️ **Diferença crucial em relação ao lançamento de frequência:**
> - Frequência: alunos já registrados são **ignorados silenciosamente**
> - Notas em lote: se **qualquer aluno** já tiver nota nesta avaliação → a operação **inteira falha** com HTTP 422, **nenhuma nota é salva**
>
> Isso deve ser comunicado claramente na UI antes do submit.

### 4. Soft delete com comportamento diferente por recurso

| Recurso | DELETE | Roles |
|---------|--------|-------|
| Avaliação | Soft delete (`ativo=false`) — histórico preservado | ADMINISTRADOR, COORDENADOR |
| AlunoAvaliacao (nota individual) | Soft delete (`ativo=false`) — pode relançar depois | ADMINISTRADOR apenas |

### 5. Boletim — agregação completa em uma chamada

`GET /v1/alunos/{id}/boletim` retorna em uma única resposta:
- Resumo de frequência global (percentual, risco LDB)
- Média ponderada por disciplina
- Situação em cada disciplina

O frontend não precisa fazer múltiplas chamadas para montar o boletim.

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/avaliacao/
├── types.ts                 ← AvaliacaoDTO, AlunoAvaliacaoDTO, NotaLancamentoDTO,
│                               BoletimResponseDTO, MediaDisciplinaDTO, SituacaoAluno
├── avaliacaoService.ts      ← funções Axios para todos os endpoints
└── avaliacaoQueries.ts      ← hooks TanStack Query
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── avaliacoes/
│   ├── page.tsx                  ← Lista de avaliações + criar/editar/desativar
│   └── [id]/
│       └── notas/
│           └── page.tsx          ← Lançamento em lote de notas da avaliação
│
└── alunos/
    └── [id]/
        └── boletim/
            └── page.tsx          ← Boletim completo do aluno
```

> A rota `/alunos/[id]` já foi criada no domínio Pessoa — o boletim é uma sub-rota nova.

---

## Tela 1 — Lista de Avaliações (`/avaliacoes`)

**Tipo:** Server Component passando dados para Client Component.

**Conteúdo:**
- `PageHeader` "Avaliações" + botão **+ Nova Avaliação** (ADMINISTRADOR, COORDENADOR, PROFESSOR)
- `Table` com colunas: **ID** | **Disciplina** | **Assunto** | **Data** | **Peso** | **Calendário** | **Ações**
  - "Disciplina": nome resolvido via lookup `GET /v1/disciplinas`
  - "Calendário": exibir `idCalendarioEscolar` ou "—" se ausente
  - "Peso": exibir valor ou "—" se null (backend assume 1 no cálculo)
- Ações por linha:
  - Ícone **Lançar Notas** → navega para `/avaliacoes/[id]/notas` (ADMINISTRADOR, PROFESSOR)
  - Ícone editar (ADMINISTRADOR, COORDENADOR, PROFESSOR)
  - Ícone desativar (ADMINISTRADOR, COORDENADOR)
- `AlertDialog` de desativação com texto: "Desativar esta avaliação? As notas vinculadas serão preservadas no histórico."

**Dialog de criar/editar Avaliação:**
- `Select` de Disciplina — obrigatório, lookup `GET /v1/disciplinas`
- `Select` de Calendário Escolar — opcional, lookup `GET /v1/calendarios-escolares`, label: "Mês — Período"
- `Input` de Assunto (`materia`) — opcional, max 255
- `Input type="date"` de Data (`dia`) — opcional, enviar como string "YYYY-MM-DD"
- `Input type="number"` de Peso — opcional, inteiro positivo, hint: "Ex: 7 para prova, 3 para trabalho"
  - Placeholder: "Sem peso (usa 1 no cálculo)"

---

## Tela 2 — Lançamento de Notas em Lote (`/avaliacoes/[id]/notas`) ← TELA PRINCIPAL

Esta é a **tela operacional do professor para registrar notas** de uma avaliação inteira.

### Estrutura da tela

**Cabeçalho:**
- Nome da avaliação (Disciplina + Assunto + Data)
- Peso da avaliação: `Badge` com o valor ou "Peso: 1 (padrão)"
- Botão **← Voltar para Avaliações**

**Aviso de transacionalidade (banner no topo da tabela):**
```tsx
<Alert>
  <AlertTitle>Atenção — operação transacional</AlertTitle>
  <AlertDescription>
    Se algum aluno já tiver nota nesta avaliação, o lançamento inteiro será cancelado.
    Verifique antes de confirmar.
  </AlertDescription>
</Alert>
```

**Como carregar os alunos:**
A avaliação pertence a uma Disciplina, que pertence a uma Classe, que pertence a uma Turma. Para listar os alunos, o fluxo é:
1. Buscar a avaliação: `GET /v1/avaliacoes/{id}` → obtém `idDisciplina` e `idCalendarioEscolar`
2. Buscar a classe via calendário: `GET /v1/classes` → filtrar por `idCalendarioEscolar` (ou pelo `idClasse` da avaliação)
3. Buscar alunos da turma: `GET /v1/turmas/{idTurma}/alunos`

> Alternativa simplificada para MVP: exibir um `Input` para o professor informar o ID da turma manualmente para carregar os alunos.

**Tabela de notas:**
- Colunas: **ID Aluno** | **Nota (0–10)** | **Observação** | **Status**
- Cada linha tem:
  - `Input type="number"` para nota — `min=0`, `max=10`, `step=0.1`
  - `Input` de obs — opcional, max 255
  - Coluna Status: vazio inicialmente; após submit mostra ✓ ou ✗ por aluno

**Estado local:**
```typescript
// Uma entrada por aluno
const [notas, setNotas] = useState<Record<number, { nota: string; obs: string }>>({});

// Ao submeter — montar o array de NotaLancamentoDTO
const payload: NotaLancamentoDTO[] = Object.entries(notas)
  .filter(([_, v]) => v.nota !== '')
  .map(([idAluno, v]) => ({
    idAluno: Number(idAluno),
    nota: parseFloat(v.nota),
    obs: v.obs || undefined,
  }));
```

**Botão "Lançar Notas":**
- `disabled` se nenhuma nota preenchida ou `isPending`
- Após sucesso: toast "Notas lançadas com sucesso" + limpar estado
- Em caso de HTTP 422: exibir toast com a mensagem do backend (indica qual aluno já tinha nota)

---

## Tela 3 — Boletim do Aluno (`/alunos/[id]/boletim`)

Acessível por **todos os roles** incluindo ALUNO e RESPONSAVEL.

**Carregamento:** `GET /v1/alunos/{id}/boletim` → retorna `BoletimResponseDTO` completo.

### Layout do Boletim

**Card 1 — Cabeçalho do aluno:**
```tsx
<Card>
  <CardHeader>
    <CardTitle>{boletim.nomeAluno}</CardTitle>
    <CardDescription>Boletim Escolar — ID {boletim.idAluno}</CardDescription>
  </CardHeader>
</Card>
```

**Card 2 — Resumo de Frequência** (reutilizar lógica da tela de frequência do aluno):
```tsx
<Card>
  <CardHeader>
    <CardTitle>Frequência Global</CardTitle>
    {boletim.frequencia.emRiscoReprovacao && (
      <Badge variant="destructive">⚠ Risco de Reprovação (LDB Art. 24)</Badge>
    )}
  </CardHeader>
  <CardContent>
    <Progress value={boletim.frequencia.percentualPresenca} />
    <p>{boletim.frequencia.percentualPresenca.toFixed(1)}% de presença</p>
    <p>Mínimo legal: 75%</p>
    <div className="grid grid-cols-3 gap-4 text-sm">
      <div>Presenças: {boletim.frequencia.totalPresencas}</div>
      <div>Faltas: {boletim.frequencia.totalFaltas}</div>
      <div>Justificadas: {boletim.frequencia.totalFaltasJust}</div>
    </div>
  </CardContent>
</Card>
```

**Card 3 — Desempenho por Disciplina** (uma linha por disciplina):
```tsx
<Table>
  <TableHeader>
    <TableRow>
      <TableHead>Disciplina</TableHead>
      <TableHead>Média</TableHead>
      <TableHead>Situação</TableHead>
    </TableRow>
  </TableHeader>
  <TableBody>
    {boletim.disciplinas.map((d) => (
      <TableRow key={d.idDisciplina}>
        <TableCell>{d.nomeDisciplina}</TableCell>
        <TableCell>
          <span className={d.mediaCalculada >= 5 ? 'text-green-600 font-bold' : 'text-red-600 font-bold'}>
            {d.mediaCalculada.toFixed(1)}
          </span>
        </TableCell>
        <TableCell>
          <SituacaoBadge situacao={d.situacao} />
        </TableCell>
      </TableRow>
    ))}
    {boletim.disciplinas.length === 0 && (
      <TableRow>
        <TableCell colSpan={3} className="text-center text-muted-foreground">
          Nenhuma nota lançada ainda
        </TableCell>
      </TableRow>
    )}
  </TableBody>
</Table>
```

**Significado das situações — exibir legenda no rodapé do card:**
```tsx
<div className="text-xs text-muted-foreground space-y-1 mt-4">
  <p>✅ <strong>Aprovado</strong>: média ≥ 5,0 e frequência ≥ 75%</p>
  <p>🟡 <strong>Em Recuperação</strong>: média entre 3,0 e 4,9</p>
  <p>🔴 <strong>Reprovado (Nota)</strong>: média inferior a 3,0</p>
  <p>🔴 <strong>Reprovado (Frequência)</strong>: menos de 75% de presença (LDB Art. 24)</p>
</div>
```

---

## Padrão de service

```typescript
// src/features/avaliacao/avaliacaoService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { AvaliacaoDTO, AlunoAvaliacaoDTO, NotaLancamentoDTO, BoletimResponseDTO } from './types';

export async function listarAvaliacoes(): Promise<AvaliacaoDTO[]> {
  const res = await api.get<ApiResponse<AvaliacaoDTO[]>>('/v1/avaliacoes');
  return res.data.data ?? [];
}

/**
 * Lançamento em lote — transacional.
 * Se qualquer aluno já tiver nota, a operação INTEIRA falha.
 */
export async function lancarNotasEmLote(
  idAvaliacao: number,
  notas: NotaLancamentoDTO[]
): Promise<AlunoAvaliacaoDTO[]> {
  try {
    const res = await api.post<ApiResponse<AlunoAvaliacaoDTO[]>>(
      `/v1/avaliacoes/${idAvaliacao}/notas`,
      notas
    );
    return res.data.data ?? [];
  } catch (error: any) {
    // Mensagem do backend indica qual aluno já tinha nota
    throw new Error(error.response?.data?.error ?? 'Erro ao lançar notas');
  }
}

export async function corrigirNota(
  id: number,
  nota: number,
  obs?: string
): Promise<AlunoAvaliacaoDTO> {
  const res = await api.put<ApiResponse<AlunoAvaliacaoDTO>>(
    `/v1/alunos-avaliacao/${id}`,
    { nota, obs }
  );
  return res.data.data!;
}

export async function buscarBoletim(idAluno: number): Promise<BoletimResponseDTO> {
  const res = await api.get<ApiResponse<BoletimResponseDTO>>(`/v1/alunos/${idAluno}/boletim`);
  return res.data.data!;
}

export async function listarNotasDoAluno(idAluno: number): Promise<AlunoAvaliacaoDTO[]> {
  const res = await api.get<ApiResponse<AlunoAvaliacaoDTO[]>>(`/v1/alunos/${idAluno}/notas`);
  return res.data.data ?? [];
}

export async function desativarAvaliacao(id: number): Promise<void> {
  try {
    await api.delete(`/v1/avaliacoes/${id}`);
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao desativar avaliação');
  }
}

export async function desativarNota(id: number): Promise<void> {
  try {
    await api.delete(`/v1/alunos-avaliacao/${id}`);
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao desativar nota');
  }
}
```

---

## Query keys (usar consistentemente)

```typescript
['avaliacoes']                            // lista de avaliações
['avaliacao', id]                         // detalhe de uma avaliação
['alunos-avaliacao']                      // todos os registros de nota ativos
['alunos-avaliacao', 'aluno', idAluno]    // notas de um aluno via /v1/alunos/{id}/notas
['boletim', idAluno]                      // boletim completo do aluno
['disciplinas']                           // lookup para Select (do domínio turma)
['calendarios-escolares']                 // lookup para Select (do domínio calendário)
```

---

## Validação Zod

```typescript
// Avaliação
const schemaAvaliacao = z.object({
  idDisciplina: z.coerce.number({ required_error: 'Disciplina é obrigatória' }),
  idCalendarioEscolar: z.coerce.number().optional().nullable(),
  materia: z.string().max(255).optional(),
  dia: z.string().regex(/^\d{4}-\d{2}-\d{2}$/).optional().or(z.literal('')),
  peso: z.coerce.number().int().positive().optional().nullable(),
});

// Lançamento em lote — validação de cada nota antes de enviar
const schemaNotaLinha = z.object({
  nota: z.coerce
    .number({ required_error: 'Nota é obrigatória' })
    .min(0, 'Mínimo 0')
    .max(10, 'Máximo 10'),
  obs: z.string().max(255).optional(),
});

// Correção individual
const schemaCorrecao = z.object({
  nota: z.coerce.number().min(0).max(10),
  obs: z.string().max(255).optional(),
});
```

---

## Controle de acesso

```typescript
const { hasAny, is } = useRoles();

// Criar/editar avaliação — ADMINISTRADOR, COORDENADOR, PROFESSOR
{hasAny('ADMINISTRADOR', 'COORDENADOR', 'PROFESSOR') && <Button>+ Nova Avaliação</Button>}

// Lançar notas em lote — ADMINISTRADOR e PROFESSOR
{hasAny('ADMINISTRADOR', 'PROFESSOR') && <Button>Lançar Notas</Button>}

// Desativar avaliação — ADMINISTRADOR e COORDENADOR (sem PROFESSOR)
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button variant="ghost">Desativar</Button>}

// Desativar nota individual — ADMINISTRADOR apenas
{is('ADMINISTRADOR') && <Button variant="ghost">Desativar Nota</Button>}

// Boletim — acessível por TODOS — não requer guard de role
// Apenas garantir que o usuário está autenticado (middleware já faz isso)
```

---

## Estados de UI obrigatórios

| Situação | Comportamento |
|----------|--------------|
| Avaliação sem peso | Exibir "—" na tabela; hint "Peso 1 será usado no cálculo" no Dialog |
| Lançamento em progresso | `disabled={isPending}` + "Lançando..." no botão |
| HTTP 422 no lote | Toast destrutivo com a mensagem do backend (indica o aluno problemático) |
| Boletim sem disciplinas | Linha na tabela: "Nenhuma nota lançada ainda" |
| Boletim com risco LDB | Badge "⚠ Risco de Reprovação" no card de frequência |
| Soft delete de avaliação | AlertDialog explicando que as notas são preservadas |
| Soft delete de nota | AlertDialog: "Desativar esta nota? Poderá ser relançada posteriormente." |
| Carregando boletim | Skeleton no lugar das duas cards |

---

## O que NÃO fazer

- Não recalcular a média ponderada no frontend — o backend retorna `mediaCalculada` já calculado
- Não recalcular a `SituacaoAluno` — o backend retorna `situacao` já determinado
- Não tratar o lançamento em lote como "ignorar duplicados" — ele **falha tudo** se houver qualquer duplicado
- Não usar o verbo "Excluir" — usar sempre **"Desativar"** (soft delete em ambos os recursos)
- Não confundir os dois endpoints de lançamento:
  - `POST /v1/avaliacoes/{id}/notas` ← lançamento em lote (preferido, transacional)
  - `POST /v1/alunos-avaliacao` ← lançamento individual (evitar na UI principal)
- Não esquecer o banner de aviso transacional antes do submit do lançamento em lote
- Não bloquear o boletim por role — ALUNO e RESPONSAVEL têm acesso ao próprio boletim

---

## Checklist de entrega

- [ ] `src/features/avaliacao/types.ts` — todos os DTOs: `AvaliacaoDTO`, `AlunoAvaliacaoDTO`, `NotaLancamentoDTO`, `BoletimResponseDTO`, `MediaDisciplinaDTO`, `SituacaoAluno`
- [ ] `src/features/avaliacao/avaliacaoService.ts` — funções para todos os endpoints
- [ ] `src/features/avaliacao/avaliacaoQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `src/app/(dashboard)/avaliacoes/page.tsx` — lista com CRUD + botão "Lançar Notas" por linha
- [ ] Dialog de criar/editar com campo `peso` e hint explicativo
- [ ] `src/app/(dashboard)/avaliacoes/[id]/notas/page.tsx` — lançamento em lote com banner transacional
- [ ] Tabela de notas com `Input type="number"` por aluno (0–10, step 0.1)
- [ ] Toast com mensagem de erro do backend quando HTTP 422 no lançamento em lote
- [ ] `src/app/(dashboard)/alunos/[id]/boletim/page.tsx` — boletim completo
- [ ] Card de frequência com `Progress` e badge de risco
- [ ] Tabela de disciplinas com `SituacaoBadge` (componente do INSTRUCTIONS_FRONTEND.md)
- [ ] Legenda das situações no rodapé do card de disciplinas
- [ ] Boletim acessível por ALUNO e RESPONSAVEL (sem guard de role além de autenticação)
- [ ] Soft delete com texto "Desativar" em ambos os AlertDialogs (Avaliação e Nota)
- [ ] Loading Skeleton no boletim e nas listas
