# Prompt para o Claude Code — Frontend: CRUD do domínio Calendário

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Calendário** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Contexto do domínio

O domínio `calendario` tem 4 recursos. O **CalendarioEscolar** é a entidade central e operacional — representa uma entrada de calendário para uma `Classe` específica em um determinado `Mes`, `Periodo` e `AnoCalendario`. É exatamente este recurso que o domínio de frequência consome: `POST /v1/frequencias/turma/{idTurma}/calendario/{idCalendarioEscolar}`.

Os outros 3 recursos (`AnoCalendario`, `Mes`, `Periodo`) são tabelas de apoio (lookups) usadas como FKs no CalendarioEscolar.

---

## Endpoints e DTOs

### 1. CalendarioEscolar — `/v1/calendarios-escolares`
```typescript
interface CalendarioEscolarDTO {
  idCalendarioEscolar?: number;
  idMes: number;              // obrigatório — FK para Mes
  idAnoCalendario?: number;   // opcional    — FK para AnoCalendario
  idPeriodo: number;          // obrigatório — FK para Periodo
  idClasse: number;           // obrigatório — FK para Classe (do domínio turma)
  diasLetivos?: string;       // texto livre, max 255 — ex: "20" ou lista de datas
  diasAvaliacoes?: string;    // texto livre, max 255 — ex: "5" ou lista de datas
}
```

| Método | Rota | Roles |
|--------|------|-------|
| GET | `/v1/calendarios-escolares` | ADMINISTRADOR, COORDENADOR, **PROFESSOR** |
| GET | `/v1/calendarios-escolares/{id}` | ADMINISTRADOR, COORDENADOR, **PROFESSOR** |
| POST | `/v1/calendarios-escolares` | ADMINISTRADOR, COORDENADOR |
| PUT | `/v1/calendarios-escolares/{id}` | ADMINISTRADOR, COORDENADOR |
| DELETE | `/v1/calendarios-escolares/{id}` | **ADMINISTRADOR apenas** |

> **Atenção:** PROFESSOR pode ler o calendário (necessário para lançar frequência), mas **não pode criar, editar ou excluir**.

### 2. AnoCalendario — `/v1/anos-calendario`
```typescript
interface AnoCalendarioDTO {
  idAnoCalendario?: number;
  ano: string; // obrigatório, max 4 — ex: "2025"
}
```
Listar/Buscar: ADMINISTRADOR, COORDENADOR. Criar/Atualizar/Deletar: **ADMINISTRADOR apenas**.

### 3. Mes — `/v1/meses`
```typescript
interface MesDTO {
  idMes?: number;
  nome: string; // obrigatório, max 255 — ex: "Janeiro", "Fevereiro"
}
```
Listar/Buscar: ADMINISTRADOR, COORDENADOR. Criar/Atualizar/Deletar: **ADMINISTRADOR apenas**.

### 4. Periodo — `/v1/periodos`
```typescript
interface PeriodoDTO {
  idPeriodo?: number;
  nome: string; // obrigatório, max 255 — ex: "1º Bimestre", "1º Semestre"
}
```
Listar/Buscar: ADMINISTRADOR, COORDENADOR. Criar/Atualizar/Deletar: **ADMINISTRADOR apenas**.

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/calendario/
├── types.ts                  ← 4 DTOs TypeScript
├── calendarioService.ts      ← funções Axios para todos os 4 recursos
└── calendarioQueries.ts      ← hooks TanStack Query
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── calendario/
│   └── page.tsx              ← Lista de CalendarioEscolar (Server Component)
│
└── admin/
    └── calendario/
        └── page.tsx          ← Tabs: AnoCalendario | Mes | Periodo
```

---

## Tela 1 — Lista do Calendário Escolar (`/calendario`)

**Tipo:** Server Component passando dados para Client Component.

**Conteúdo:**
- `PageHeader` com título "Calendário Escolar" e botão **+ Novo Calendário** (ADMINISTRADOR, COORDENADOR apenas — oculto para PROFESSOR)
- `Table` com colunas: **ID** | **Mês** | **Ano** | **Período** | **Classe** | **Dias Letivos** | **Dias Avaliações** | **Ações**
  - "Mês": exibir `nome` resolvido do Mes (`GET /v1/meses`)
  - "Ano": exibir `ano` resolvido do AnoCalendario (`GET /v1/anos-calendario`) — ou "—" se `idAnoCalendario` for null
  - "Período": exibir `nome` resolvido do Periodo (`GET /v1/periodos`)
  - "Classe": exibir o `idClasse` (a Classe é do domínio turma — exibir o ID por enquanto)
- Filtros client-side acima da tabela:
  - `Select` de Ano — opções de `GET /v1/anos-calendario`
  - `Select` de Período — opções de `GET /v1/periodos`
- Ações por linha:
  - Ícone de editar (ADMINISTRADOR, COORDENADOR)
  - Ícone de excluir (ADMINISTRADOR apenas)
- Dialog de criar/editar com todos os campos (ver detalhes abaixo)
- `AlertDialog` de confirmação antes de excluir

**Dialog de criar/editar CalendarioEscolar:**
- `Select` de Mês — obrigatório, lookup `GET /v1/meses`, exibir `nome`
- `Select` de Ano Calendário — opcional, lookup `GET /v1/anos-calendario`, exibir `ano` + opção "Sem ano definido"
- `Select` de Período — obrigatório, lookup `GET /v1/periodos`, exibir `nome`
- `Select` de Classe — obrigatório, lookup `GET /v1/classes`, exibir `idClasse` (identificador único — ver nota abaixo)
- `Input` de Dias Letivos — opcional, texto livre (ex: "20")
- `Input` de Dias Avaliações — opcional, texto livre (ex: "5")
- Os 4 lookups carregados em paralelo via `useQueries` ou `Promise.all`

> **Nota sobre Classe no Select:** A `ClasseDTO` (`GET /v1/classes`) retorna apenas IDs de FK, sem campos descritivos próprios. Exibir no Select como `"Classe #${idClasse}"`. Em versão futura, o join com Turma, Curso e Professor permitirá uma descrição mais rica.

---

## Tela 2 — Configurações Administrativas (`/admin/calendario`)

Página única com **Tabs** (shadcn/ui) em 3 abas. Todas as abas exigem **ADMINISTRADOR** para escrita — COORDENADOR pode ver mas não editar.

### Aba 1 — Anos Calendário

- `Table`: **ID** | **Ano** | **Ações**
- Dialog criar/editar: campo `ano` obrigatório, `Input` com `maxLength={4}`, placeholder "2025"
- Validação: apenas 4 dígitos numéricos
- `AlertDialog` exclusão

### Aba 2 — Meses

- `Table`: **ID** | **Nome** | **Ações**
- Dialog criar/editar: campo `nome` obrigatório
- Sugestão de UX: ao criar, oferecer um `Select` com os 12 meses pré-definidos ("Janeiro" … "Dezembro") + opção "Outro" que abre campo livre — mas enviar sempre como string no campo `nome`
- `AlertDialog` exclusão

### Aba 3 — Períodos

- `Table`: **ID** | **Nome** | **Ações**
- Dialog criar/editar: campo `nome` obrigatório
- Exemplos de valores esperados: "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "1º Semestre", "Anual"
- `AlertDialog` exclusão

---

## Controle de acesso

```typescript
// Guard SSR no layout /admin/ — já criado em domínios anteriores (reutilizar)

// Na lista principal /calendario:
const { hasAny, is } = useRoles();

// Botão "+ Novo Calendário" — ADMINISTRADOR e COORDENADOR
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button>+ Novo Calendário</Button>}

// Ícone de editar linha — ADMINISTRADOR e COORDENADOR
{hasAny('ADMINISTRADOR', 'COORDENADOR') && <Button variant="ghost" size="icon">...</Button>}

// Ícone de excluir — apenas ADMINISTRADOR
{is('ADMINISTRADOR') && <Button variant="ghost" size="icon">...</Button>}

// Nas abas de /admin/calendario: botões de escrita apenas para ADMINISTRADOR
{is('ADMINISTRADOR') && <Button>+ Novo</Button>}
```

---

## Validação Zod por recurso

```typescript
// CalendarioEscolar
const schemaCalendario = z.object({
  idMes:            z.coerce.number({ required_error: 'Mês é obrigatório' }),
  idAnoCalendario:  z.coerce.number().optional().nullable(),
  idPeriodo:        z.coerce.number({ required_error: 'Período é obrigatório' }),
  idClasse:         z.coerce.number({ required_error: 'Classe é obrigatória' }),
  diasLetivos:      z.string().max(255).optional(),
  diasAvaliacoes:   z.string().max(255).optional(),
});

// AnoCalendario — validar 4 dígitos numéricos
const schemaAno = z.object({
  ano: z.string()
    .min(4, 'Informe o ano com 4 dígitos')
    .max(4, 'Máximo 4 caracteres')
    .regex(/^\d{4}$/, 'Use apenas números — ex: 2025'),
});

// Mes / Periodo
const schemaNome = z.object({
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
});
```

---

## Padrão de service

```typescript
// src/features/calendario/calendarioService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { CalendarioEscolarDTO, MesDTO, PeriodoDTO, AnoCalendarioDTO } from './types';

export async function listarCalendarios(): Promise<CalendarioEscolarDTO[]> {
  const res = await api.get<ApiResponse<CalendarioEscolarDTO[]>>('/v1/calendarios-escolares');
  return res.data.data ?? [];
}

export async function criarCalendario(
  dto: Omit<CalendarioEscolarDTO, 'idCalendarioEscolar'>
): Promise<CalendarioEscolarDTO> {
  try {
    const res = await api.post<ApiResponse<CalendarioEscolarDTO>>('/v1/calendarios-escolares', dto);
    return res.data.data!;
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao criar calendário');
  }
}

export async function listarMeses(): Promise<MesDTO[]> {
  const res = await api.get<ApiResponse<MesDTO[]>>('/v1/meses');
  return res.data.data ?? [];
}

export async function listarPeriodos(): Promise<PeriodoDTO[]> {
  const res = await api.get<ApiResponse<PeriodoDTO[]>>('/v1/periodos');
  return res.data.data ?? [];
}

export async function listarAnosCalendario(): Promise<AnoCalendarioDTO[]> {
  const res = await api.get<ApiResponse<AnoCalendarioDTO[]>>('/v1/anos-calendario');
  return res.data.data ?? [];
}

// Repetir padrão de criar/atualizar/deletar para AnoCalendario, Mes, Periodo
```

---

## Query keys (usar consistentemente)

```typescript
['calendarios-escolares']          // lista completa
['calendarios-escolares', filtros] // com filtro de ano/período (client-side, não precisa de key separada)
['anos-calendario']                // lookup para Select e para resolver nomes na tabela
['meses']                          // lookup para Select e para resolver nomes na tabela
['periodos']                       // lookup para Select e para resolver nomes na tabela
['classes']                        // lookup para Select no Dialog (do domínio turma)
```

> Os lookups `['anos-calendario']`, `['meses']` e `['periodos']` são os mesmos usados no Dialog do CalendarioEscolar e nas tabelas das abas admin — o cache do TanStack Query evita buscas duplicadas automaticamente.

---

## Relação com o domínio de Frequência

O `idCalendarioEscolar` é o elo entre Calendário e Frequência. O professor usa o seguinte fluxo:

1. Acessa `/calendario` e localiza a entrada do dia (Mês + Período + Classe desejada)
2. Copia o `idCalendarioEscolar`
3. Usa esse ID ao lançar frequência via `POST /v1/frequencias/turma/{idTurma}/calendario/{idCalendarioEscolar}`

No frontend, a tela de lançamento de frequência (domínio turma) deve carregar a lista de CalendarioEscolar e apresentar um `Select` para o professor escolher a aula — exibindo "Mês / Período" como label.

---

## O que NÃO fazer

- Não tratar `diasLetivos` e `diasAvaliacoes` como números — o backend os armazena como `String`. Usar `Input` de texto, não `Input type="number"`
- Não exigir `idAnoCalendario` na validação — é o único campo não obrigatório do CalendarioEscolar
- Não criar rotas separadas para Mes, Periodo e AnoCalendario — tudo em Tabs em `/admin/calendario`
- Não permitir que PROFESSOR acesse os botões de editar/excluir na lista — apenas leitura
- Não buscar os lookups sequencialmente — carregar Mes, Periodo, AnoCalendario e Classe em paralelo no Dialog

---

## Checklist de entrega

- [ ] `src/features/calendario/types.ts` — 4 DTOs TypeScript
- [ ] `src/features/calendario/calendarioService.ts` — funções Axios para os 4 recursos
- [ ] `src/features/calendario/calendarioQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `src/app/(dashboard)/calendario/page.tsx` — lista com filtros de Ano e Período + Dialog criar/editar
- [ ] Dialog com 4 Selects (Mes, Ano, Periodo, Classe) carregados em paralelo
- [ ] Nomes resolvidos nas colunas Mês, Ano e Período da tabela (join client-side com lookup)
- [ ] Filtros client-side por Ano e Período acima da tabela
- [ ] Ações de editar (ADMINISTRADOR, COORDENADOR) e excluir (ADMINISTRADOR) por linha
- [ ] `src/app/(dashboard)/admin/calendario/page.tsx` — Tabs: AnoCalendario | Mes | Periodo
- [ ] Validação do campo `ano` com regex `/^\d{4}$/`
- [ ] Botões de escrita em admin ocultos para COORDENADOR
- [ ] Loading Skeleton em todas as listas
- [ ] Toast de erro com campo `error` da ApiResponse
