# Prompt para o Claude Code — Frontend: CRUD do domínio Frequência

> Cole este prompt no projeto frontend (Next.js). Leia o INSTRUCTIONS_FRONTEND.md integralmente antes de começar.

---

Implemente o **CRUD completo do domínio Frequência** para o DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Esse domínio já está parcialmente documentado no INSTRUCTIONS_FRONTEND.md (seções 6.4 e 9.1) — use como referência. Toda decisão de arquitetura, padrão de código, shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Análise do backend — lógica já implementada

O domínio `frequencia` tem **1 controller, 1 service e 2 DTOs**. A lógica de negócio já está toda no backend:

### Endpoints disponíveis

| Método | Rota | Roles | Descrição |
|--------|------|-------|-----------|
| GET | `/v1/frequencias` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Lista todos os registros ativos |
| GET | `/v1/frequencias/{id}` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Busca por ID |
| GET | `/v1/frequencias/aluno/{idAluno}` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Histórico do aluno |
| GET | `/v1/frequencias/aluno/{idAluno}/resumo` | ADMINISTRADOR, COORDENADOR, PROFESSOR | Resumo com percentual LDB |
| POST | `/v1/frequencias` | ADMINISTRADOR, **PROFESSOR** | Lança para um aluno individualmente |
| POST | `/v1/frequencias/turma/{idTurma}/calendario/{idCalendario}` | ADMINISTRADOR, **PROFESSOR** | **Lança em lote para toda a turma** ← endpoint principal |
| PUT | `/v1/frequencias/{id}` | ADMINISTRADOR, **PROFESSOR** | Corrige o tipo de um registro |
| DELETE | `/v1/frequencias/{id}` | **ADMINISTRADOR apenas** | Soft delete |

> **Destaque:** PROFESSOR pode lançar e corrigir frequência, mas **não pode deletar** (soft delete é exclusivo do ADMINISTRADOR).

### DTOs

```typescript
type TipoFrequencia = 'PRESENTE' | 'FALTA' | 'FALTA_JUSTIFICADA';

interface AlunoFrequenciaDTO {
  idAlunoFrequencia?: number;
  idAluno: number;              // obrigatório
  idCalendarioEscolar: number;  // obrigatório
  tipoFrequencia?: TipoFrequencia; // opcional — backend assume PRESENTE se omitido
}

interface FrequenciaResumoDTO {
  idAluno: number;
  totalAulas: number;           // denominador da fórmula
  totalPresencas: number;
  totalFaltas: number;
  totalFaltasJust: number;
  percentualPresenca: number;   // 0.0 a 100.0
  emRiscoReprovacao: boolean;   // true se percentualPresenca < 75.0
}
```

### Regras de negócio implementadas no backend (crítico para o frontend)

**1. TipoFrequencia — 3 valores com semântica diferente (LDB Art. 24):**
- `PRESENTE` — conta como presença. Percentual sobe.
- `FALTA` — ausência injustificada. Conta para reprovação.
- `FALTA_JUSTIFICADA` — ausência com justificativa. **Não conta para reprovação**, mas a aula ainda ocorreu (o denominador não muda). Fórmula: `percentual = (totalPresencas / totalAulas) * 100`

**2. Lançamento em lote — endpoint mais importante:**
```
POST /v1/frequencias/turma/{idTurma}/calendario/{idCalendario}?tipoPadrao=PRESENTE
Body: { "5": "FALTA", "7": "FALTA_JUSTIFICADA" }  ← só as exceções
```
- O body é um `Map<idAluno, TipoFrequencia>` — **apenas as exceções** ao `tipoPadrao`
- Alunos não incluídos no body recebem o `tipoPadrao` (default: `PRESENTE`)
- Alunos que **já têm frequência registrada** para esta aula são **silenciosamente ignorados** (sem erro)
- HTTP 422 se a turma não tiver nenhum aluno matriculado

**3. Duplicidade — lançamento individual:**
- `POST /v1/frequencias` retorna HTTP 422 se o aluno já tem frequência ativa para aquela aula
- Mensagem do backend: `"Frequência já registrada para este aluno nesta aula. Use PUT /v1/frequencias/{id} para corrigir."`
- Para corrigir: usar `PUT /v1/frequencias/{id}` com o novo `tipoFrequencia`

**4. Soft delete:**
- `DELETE /v1/frequencias/{id}` define `ativo=false` — o histórico é preservado
- Após desativar, o lançamento pode ser refeito via POST

**5. Limite de reprovação:**
- `emRiscoReprovacao = true` quando `percentualPresenca < 75.0` (constante `FREQUENCIA_MINIMA_LDB`)
- Exibir alerta vermelho sempre que `emRiscoReprovacao` for `true`

---

## Arquitetura — O que criar

### Arquivos de feature

```
src/features/frequencia/
├── types.ts                  ← TipoFrequencia, AlunoFrequenciaDTO, FrequenciaResumoDTO
├── frequenciaService.ts      ← funções Axios para todos os endpoints
└── frequenciaQueries.ts      ← hooks TanStack Query
```

### Rotas Next.js

```
src/app/(dashboard)/
│
├── turmas/
│   └── [id]/
│       └── frequencia/
│           └── page.tsx      ← Tela principal: lançamento em lote (PROFESSOR e ADMIN)
│
├── frequencias/
│   └── page.tsx              ← Histórico global com filtro (ADMIN, COORDENADOR)
│
└── alunos/
    └── [id]/
        └── frequencia/
            └── page.tsx      ← Histórico + resumo por aluno
```

---

## Tela 1 — Lançamento em Lote (`/turmas/[id]/frequencia`) ← TELA PRINCIPAL

Esta é a **tela operacional mais importante do sistema** — usada pelo professor em cada aula.

### Fluxo completo

1. Professor acessa `/turmas/[id]/frequencia`
2. Seleciona uma **entrada do calendário** (qual aula está sendo registrada)
3. O sistema carrega **todos os alunos matriculados** na turma
4. Professor marca as **exceções** (quem está faltando)
5. Confirma — sistema envia apenas as exceções no body

### Estrutura da tela

**Cabeçalho:**
- Nome da turma (buscar via `GET /v1/turmas/{id}`)
- Botão **← Voltar para a Turma**

**Seleção de aula — `Select` obrigatório:**
- Lookup: `GET /v1/calendarios-escolares`
- Filtrar client-side pelo `idClasse` que pertence à turma (cruzar com `GET /v1/classes` onde `idTurma === id`)
- Label de cada opção: `"[Mês] — [Período]"` — resolver nomes via lookups de Mes e Periodo
- Enquanto nenhuma aula for selecionada, o restante da tela fica desabilitado

**Lista de alunos — Tabela interativa:**
- Buscar: `GET /v1/turmas/{id}/alunos`
- Colunas: **Aluno (ID)** | **Frequência**
- A coluna **Frequência** é um `ToggleGroup` (ou `RadioGroup`) com 3 opções por linha:

```tsx
// Para cada aluno, renderizar:
<ToggleGroup type="single" value={frequencias[idAluno] ?? 'PRESENTE'} onValueChange={(v) => setFrequencia(idAluno, v)}>
  <ToggleGroupItem value="PRESENTE" className="text-green-600">✓ Presente</ToggleGroupItem>
  <ToggleGroupItem value="FALTA" className="text-red-600">✗ Falta</ToggleGroupItem>
  <ToggleGroupItem value="FALTA_JUSTIFICADA" className="text-yellow-600">⚠ Justificada</ToggleGroupItem>
</ToggleGroup>
```

**Estado local do lançamento em lote:**
```typescript
// Estado: apenas as EXCEÇÕES ao tipoPadrao (PRESENTE)
const [excecoes, setExcecoes] = useState<Record<number, TipoFrequencia>>({});

// Ao mudar um aluno:
function setFrequencia(idAluno: number, tipo: TipoFrequencia) {
  if (tipo === 'PRESENTE') {
    // Remover da lista de exceções — vai receber o tipoPadrao
    setExcecoes(prev => { const next = { ...prev }; delete next[idAluno]; return next; });
  } else {
    setExcecoes(prev => ({ ...prev, [idAluno]: tipo }));
  }
}

// Ao confirmar:
// POST /v1/frequencias/turma/{idTurma}/calendario/{idCalendario}?tipoPadrao=PRESENTE
// Body: excecoes  ← apenas os alunos com FALTA ou FALTA_JUSTIFICADA
```

**Resumo antes do envio:**
- Contador em tempo real acima da tabela: `"X presente(s) · Y falta(s) · Z justificada(s)"`
- Botão **Registrar Chamada** — `disabled` se nenhuma aula selecionada ou nenhum aluno carregado

**Após sucesso:**
- Toast: `"Frequência lançada para N aluno(s)"` (vem na `message` da ApiResponse)
- Recarregar a lista para mostrar estado atualizado

**Alunos já registrados (re-lançamento):**
- O backend ignora silenciosamente alunos que já têm frequência — não retorna erro
- A tela não precisa bloquear o reenvio; o backend lida com isso

---

## Tela 2 — Histórico Global (`/frequencias`)

Acessível por ADMINISTRADOR e COORDENADOR. PROFESSOR **não acessa** esta rota (redirecionar para `/acesso-negado`).

**Conteúdo:**
- `PageHeader` "Frequências"
- `Table` com colunas: **ID** | **Aluno (ID)** | **Calendário (ID)** | **Tipo** | **Ações**
  - Coluna Tipo: usar `FrequenciaBadge` baseado no tipo
- Filtro client-side por `TipoFrequencia` (Select com as 3 opções + "Todos")
- Ações por linha:
  - Ícone de corrigir (ADMINISTRADOR, PROFESSOR) — abre Dialog com `Select` do novo tipo
  - Ícone de desativar (ADMINISTRADOR apenas) — `AlertDialog` + soft delete
- **Sem botão de criar** — o lançamento individual é feito via `POST /v1/frequencias`, mas o fluxo principal é sempre pelo lançamento em lote

**Dialog de correção individual:**
- Exibir: "Aluno ID: {idAluno} | Aula ID: {idCalendarioEscolar}"
- `Select` do novo tipo: PRESENTE / FALTA / FALTA_JUSTIFICADA
- Salvar: `PUT /v1/frequencias/{id}` com `{ tipoFrequencia: novoTipo }`

---

## Tela 3 — Histórico e Resumo do Aluno (`/alunos/[id]/frequencia`)

Esta tela é acessível por ADMINISTRADOR, COORDENADOR e PROFESSOR.

**Seção 1 — Resumo de Frequência (`GET /v1/frequencias/aluno/{id}/resumo`):**

```tsx
// Card de resumo — renderizar com base no FrequenciaResumoDTO
<Card>
  <CardHeader>
    <CardTitle>Resumo de Frequência</CardTitle>
    {resumo.emRiscoReprovacao && (
      <Badge variant="destructive">⚠ Risco de Reprovação (LDB Art. 24)</Badge>
    )}
  </CardHeader>
  <CardContent>
    <div>Total de aulas: {resumo.totalAulas}</div>
    <div>Presenças: {resumo.totalPresencas}</div>
    <div>Faltas: {resumo.totalFaltas}</div>
    <div>Faltas justificadas: {resumo.totalFaltasJust}</div>
    <div>Frequência: <strong>{resumo.percentualPresenca.toFixed(1)}%</strong></div>
    {/* Barra de progresso visual */}
    <Progress value={resumo.percentualPresenca} className={resumo.emRiscoReprovacao ? 'bg-red-100' : 'bg-green-100'} />
    <p className="text-sm text-muted-foreground">Mínimo legal: 75% (LDB Art. 24)</p>
  </CardContent>
</Card>
```

**Seção 2 — Histórico detalhado (`GET /v1/frequencias/aluno/{id}`):**
- `Table` com colunas: **ID** | **Calendário (ID)** | **Tipo** | **Ações**
- Coluna Tipo: `FrequenciaBadge` — verde (PRESENTE), vermelho (FALTA), amarelo (FALTA_JUSTIFICADA)
- Ações: corrigir tipo (ADMINISTRADOR, PROFESSOR) via Dialog + PUT

**Botão "← Voltar para o Aluno"**

---

## Componente `FrequenciaBadge` (já mencionado no INSTRUCTIONS_FRONTEND.md)

O componente `FrequenciaBadge` para o resumo já está especificado no INSTRUCTIONS_FRONTEND.md. Para o histórico, criar também um `TipoFrequenciaBadge` para o tipo de cada registro:

```tsx
// src/shared/components/TipoFrequenciaBadge.tsx
import { Badge } from '@/components/ui/badge';
import type { TipoFrequencia } from '@/features/frequencia/types';

const config: Record<TipoFrequencia, { label: string; variant: 'default' | 'destructive' | 'outline' }> = {
  PRESENTE:           { label: 'Presente',    variant: 'default' },
  FALTA:              { label: 'Falta',        variant: 'destructive' },
  FALTA_JUSTIFICADA:  { label: 'Justificada',  variant: 'outline' },
};

export function TipoFrequenciaBadge({ tipo }: { tipo: TipoFrequencia }) {
  const { label, variant } = config[tipo];
  return <Badge variant={variant}>{label}</Badge>;
}
```

---

## Padrão de service

```typescript
// src/features/frequencia/frequenciaService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { AlunoFrequenciaDTO, FrequenciaResumoDTO, TipoFrequencia } from './types';

export async function listarFrequencias(): Promise<AlunoFrequenciaDTO[]> {
  const res = await api.get<ApiResponse<AlunoFrequenciaDTO[]>>('/v1/frequencias');
  return res.data.data ?? [];
}

export async function listarPorAluno(idAluno: number): Promise<AlunoFrequenciaDTO[]> {
  const res = await api.get<ApiResponse<AlunoFrequenciaDTO[]>>(`/v1/frequencias/aluno/${idAluno}`);
  return res.data.data ?? [];
}

export async function calcularResumo(idAluno: number): Promise<FrequenciaResumoDTO> {
  const res = await api.get<ApiResponse<FrequenciaResumoDTO>>(`/v1/frequencias/aluno/${idAluno}/resumo`);
  return res.data.data!;
}

/**
 * Lança frequência em lote. Body contém apenas as exceções ao tipoPadrao.
 * Alunos já registrados são ignorados silenciosamente pelo backend.
 */
export async function lancarEmLote(
  idTurma: number,
  idCalendario: number,
  excecoes: Record<number, TipoFrequencia>,
  tipoPadrao: TipoFrequencia = 'PRESENTE'
): Promise<AlunoFrequenciaDTO[]> {
  try {
    const res = await api.post<ApiResponse<AlunoFrequenciaDTO[]>>(
      `/v1/frequencias/turma/${idTurma}/calendario/${idCalendario}`,
      excecoes,
      { params: { tipoPadrao } }
    );
    return res.data.data ?? [];
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao lançar frequência');
  }
}

export async function corrigirFrequencia(
  id: number,
  tipoFrequencia: TipoFrequencia
): Promise<AlunoFrequenciaDTO> {
  const res = await api.put<ApiResponse<AlunoFrequenciaDTO>>(
    `/v1/frequencias/${id}`,
    { tipoFrequencia }
  );
  return res.data.data!;
}

export async function desativarFrequencia(id: number): Promise<void> {
  try {
    await api.delete(`/v1/frequencias/${id}`);
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao desativar frequência');
  }
}
```

---

## Query keys (usar consistentemente)

```typescript
['frequencias']                           // lista global
['frequencias', 'aluno', idAluno]         // histórico por aluno
['frequencias', 'aluno', idAluno, 'resumo'] // resumo percentual
['turma', idTurma, 'alunos']              // alunos da turma para o lançamento em lote (do domínio turma)
['calendarios-escolares']                 // lookup de aulas para o Select
```

---

## Controle de acesso

```typescript
const { hasAny, is } = useRoles();

// Guard de rota para /frequencias (histórico global)
// Adicionar no layout ou no início do componente:
// Apenas ADMINISTRADOR e COORDENADOR — redirecionar PROFESSOR
if (!hasAny('ADMINISTRADOR', 'COORDENADOR')) redirect('/acesso-negado');

// Botão de corrigir tipo — ADMINISTRADOR e PROFESSOR
{hasAny('ADMINISTRADOR', 'PROFESSOR') && <Button variant="ghost" size="icon">Corrigir</Button>}

// Botão de desativar (soft delete) — ADMINISTRADOR apenas
{is('ADMINISTRADOR') && <Button variant="ghost" size="icon">Desativar</Button>}

// Botão de Registrar Chamada — ADMINISTRADOR e PROFESSOR
{hasAny('ADMINISTRADOR', 'PROFESSOR') && <Button>Registrar Chamada</Button>}
```

---

## Validação

```typescript
// Não há Zod complexo neste domínio — os dados vêm de toggles e selects controlados
// Validação client-side mínima antes de submeter:

function validarLancamento(idCalendario: number | null, alunos: any[]): string | null {
  if (!idCalendario) return 'Selecione uma aula antes de registrar';
  if (alunos.length === 0) return 'Nenhum aluno carregado';
  return null;
}

// Correção individual
const schemaCorrecao = z.object({
  tipoFrequencia: z.enum(['PRESENTE', 'FALTA', 'FALTA_JUSTIFICADA'], {
    required_error: 'Selecione o tipo de frequência',
  }),
});
```

---

## Estados de UI obrigatórios

| Situação | Comportamento |
|----------|--------------|
| Nenhuma aula selecionada | Lista de alunos oculta ou desabilitada |
| Carregando alunos | `Skeleton` nas linhas da tabela |
| Turma sem alunos | Mensagem: "Nenhum aluno matriculado nesta turma" |
| Contagem em tempo real | `"X presente(s) · Y falta(s) · Z justificada(s)"` acima da tabela |
| Lançamento em progresso | `disabled={isPending}` + "Registrando..." no botão |
| Sucesso do lançamento | Toast com a mensagem da API: `"Frequência lançada para N aluno(s)"` |
| HTTP 422 (turma vazia) | Toast com o `error` da ApiResponse |
| `emRiscoReprovacao = true` | Badge vermelho + texto explicativo na tela do aluno |
| Desativar registro | AlertDialog: "Desativar este registro? O histórico será preservado." |

---

## O que NÃO fazer

- Não enviar **todos** os alunos no body do lançamento em lote — enviar apenas as exceções ao `tipoPadrao`. Alunos com PRESENTE não precisam ser incluídos
- Não mostrar erro quando alunos já registrados são ignorados — o backend retorna 201 normalmente; não é um erro
- Não usar o verbo "Excluir" para o soft delete — usar sempre **"Desativar"**
- Não criar tela de criação individual via `POST /v1/frequencias` — o fluxo é sempre pelo lançamento em lote em `/turmas/[id]/frequencia`
- Não tratar `FALTA_JUSTIFICADA` como sinônimo de `FALTA` na UI — exibir em amarelo e com texto diferente
- Não esquecer o query param `?tipoPadrao=PRESENTE` na chamada do lançamento em lote (Axios: `{ params: { tipoPadrao } }`)
- Não bloquear a tela de lançamento se já existirem registros para a aula — o backend ignora os já registrados silenciosamente

---

## Checklist de entrega

- [ ] `src/features/frequencia/types.ts` — `TipoFrequencia`, `AlunoFrequenciaDTO`, `FrequenciaResumoDTO`
- [ ] `src/features/frequencia/frequenciaService.ts` — todos os endpoints incluindo `lancarEmLote`
- [ ] `src/features/frequencia/frequenciaQueries.ts` — hooks TanStack Query com query keys corretas
- [ ] `src/shared/components/TipoFrequenciaBadge.tsx` — Badge PRESENTE (verde) / FALTA (vermelho) / FALTA_JUSTIFICADA (amarelo)
- [ ] `src/app/(dashboard)/turmas/[id]/frequencia/page.tsx` — lançamento em lote com Select de aula + tabela com toggles
- [ ] Contador em tempo real de presenças/faltas/justificadas
- [ ] Estado local de exceções — enviar apenas `Record<idAluno, TipoFrequencia>` das exceções
- [ ] `src/app/(dashboard)/frequencias/page.tsx` — histórico global (ADMIN, COORDENADOR) com filtro de tipo + correção + desativação
- [ ] `src/app/(dashboard)/alunos/[id]/frequencia/page.tsx` — Card de resumo com barra de progresso + tabela de histórico + correção
- [ ] `Progress` do shadcn na tela do aluno mostrando percentual vs. linha de 75%
- [ ] Guard de rota `/frequencias` bloqueando PROFESSOR
- [ ] Soft delete com texto "Desativar" e AlertDialog explicativo
- [ ] Toast de sucesso com mensagem da API no lançamento em lote
