# Prompt para o Claude Code — Frontend: CRUD do domínio Instituição

> Cole este prompt no projeto frontend (Next.js). O Claude Code do frontend deve ler o INSTRUCTIONS_FRONTEND.md antes de iniciar.

---

Implemente o **CRUD completo do domínio Instituição** para a área administrativa do sistema DiárioDigital.

Leia o `INSTRUCTIONS_FRONTEND.md` integralmente antes de começar. Toda decisão de arquitetura, padrão de código, uso de shadcn/ui, chamadas à API e tratamento de erros deve seguir estritamente esse arquivo.

---

## Contexto do domínio

O domínio `instituicao` agrupa 6 recursos do backend. Todos exigem role `ADMINISTRADOR` para escrita e `ADMINISTRADOR` ou `COORDENADOR` para leitura.

---

## Endpoints e DTOs

### 1. InstituicaoEnsino — `/v1/instituicoes-ensino`
```typescript
interface InstituicaoEnsinoDTO {
  idInstituicaoEnsino?: number;
  descricao?: string;      // max 255, campo de texto livre
  codigoEstadual?: string; // max 255, campo de texto livre
}
```

### 2. Ensino — `/v1/ensinos`
```typescript
interface EnsinoDTO {
  idEnsino?: number;
  nome: string; // obrigatório, max 255
}
```

### 3. Grau — `/v1/graus`
```typescript
interface GrauDTO {
  idGrau?: number;
  nome: string; // obrigatório, max 255
}
```

### 4. Série — `/v1/series`
```typescript
interface SerieDTO {
  idSerie?: number;
  nome: string; // obrigatório, max 255
}
```

### 5. Turno — `/v1/turnos`
```typescript
interface TurnoDTO {
  idTurno?: number;
  nome: string; // obrigatório, max 255
}
```

### 6. Curso — `/v1/cursos`
```typescript
interface CursoDTO {
  idCurso?: number;
  idEnsino: number; // obrigatório — FK para EnsinoDTO
  idGrau: number;   // obrigatório — FK para GrauDTO
  idSerie: number;  // obrigatório — FK para SerieDTO
}
```

Todos os 6 recursos têm exatamente os mesmos 5 endpoints:
- `GET    /v1/{recurso}`      → `ApiResponse<DTO[]>`     — lista
- `GET    /v1/{recurso}/{id}` → `ApiResponse<DTO>`       — detalhe
- `POST   /v1/{recurso}`      → `ApiResponse<DTO>` + HTTP 201 — criar
- `PUT    /v1/{recurso}/{id}` → `ApiResponse<DTO>`       — atualizar
- `DELETE /v1/{recurso}/{id}` → `ApiResponse<null>` + message "Excluído com sucesso"

---

## Arquitetura — O que criar

### Arquivos de feature (lógica de negócio)

```
src/features/instituicao/
├── types.ts               ← todos os 6 DTOs TypeScript
├── instituicaoService.ts  ← funções de chamada à API (Axios)
└── instituicaoQueries.ts  ← hooks TanStack Query (useQuery + useMutation)
```

### Página única com abas

```
src/app/(dashboard)/admin/instituicao/
└── page.tsx               ← Server Component leve, importa o Client Component de abas
```

A página deve ser uma única rota (`/admin/instituicao`) com **Tabs do shadcn/ui**, uma aba por recurso na seguinte ordem:
1. Instituição de Ensino
2. Ensino
3. Grau
4. Série
5. Turno
6. Curso

---

## Padrão visual por recurso

### Recursos simples (Ensino, Grau, Série, Turno)

Cada aba deve conter:
- Um `Table` (shadcn/ui) com colunas: **ID** e **Nome**
- Botão **+ Novo** no cabeçalho que abre um `Dialog`
- Ícone de edição e de exclusão em cada linha (usar `Button variant="ghost" size="icon"`)
- Dialog único para criar e editar (mesmo componente, modo controlado por prop `item?: DTO`)
- Formulário no Dialog: campo `nome` obrigatório com validação Zod (`z.string().min(1)`)
- Confirmação de exclusão com `AlertDialog` (shadcn/ui) — nunca excluir sem confirmar

### InstituicaoEnsino

Mesmo padrão, mas com dois campos no formulário:
- `descricao` — `Textarea` (shadcn/ui), opcional, max 255
- `codigoEstadual` — `Input`, opcional, max 255

### Curso (recurso composto)

O Curso é uma composição de Ensino + Grau + Série. A aba deve:
- Exibir na tabela as colunas: **ID**, **Ensino**, **Grau**, **Série** (exibir o `nome` resolvido, não apenas o ID)
- No Dialog de criar/editar, usar `Select` (shadcn/ui) para cada FK:
  - Select de Ensino: carregar `GET /v1/ensinos` → exibir `nome`, value = `idEnsino`
  - Select de Grau: carregar `GET /v1/graus` → exibir `nome`, value = `idGrau`
  - Select de Série: carregar `GET /v1/series` → exibir `nome`, value = `idSerie`
- Todos os 3 Selects são obrigatórios (validação Zod)
- Os 3 listagens de lookup devem ser carregadas em paralelo via `Promise.all` ou `useQueries`

---

## Controle de acesso

- A rota `/admin/instituicao` deve ser protegida por um `layout.tsx` no route group `/admin/` que verifica se `user.role === 'ADMINISTRADOR'`. Se não for, redirecionar para `/acesso-negado`.
- No nível de componente: o botão **+ Novo**, os ícones de editar e excluir devem ser ocultados usando o hook `useRoles()` (`hasAny('ADMINISTRADOR')`) — para que o COORDENADOR possa visualizar mas não alterar.

```typescript
// Exemplo de uso nos botões de ação
const { hasAny } = useRoles();
// ...
{hasAny('ADMINISTRADOR') && (
  <Button variant="ghost" size="icon" onClick={() => setEditando(item)}>
    <Pencil className="h-4 w-4" />
  </Button>
)}
```

---

## Padrão de service (seguir exatamente este modelo)

```typescript
// src/features/instituicao/instituicaoService.ts
import api from '@/shared/api/axiosInstance';
import type { ApiResponse } from '@/shared/api/types';
import type { EnsinoDTO } from './types';

export async function listarEnsinos(): Promise<EnsinoDTO[]> {
  const res = await api.get<ApiResponse<EnsinoDTO[]>>('/v1/ensinos');
  return res.data.data ?? [];
}

export async function criarEnsino(dto: Omit<EnsinoDTO, 'idEnsino'>): Promise<EnsinoDTO> {
  const res = await api.post<ApiResponse<EnsinoDTO>>('/v1/ensinos', dto);
  return res.data.data!;
}

export async function atualizarEnsino(id: number, dto: Omit<EnsinoDTO, 'idEnsino'>): Promise<EnsinoDTO> {
  const res = await api.put<ApiResponse<EnsinoDTO>>(`/v1/ensinos/${id}`, dto);
  return res.data.data!;
}

export async function deletarEnsino(id: number): Promise<void> {
  try {
    await api.delete(`/v1/ensinos/${id}`);
  } catch (error: any) {
    throw new Error(error.response?.data?.error ?? 'Erro ao excluir');
  }
}
// Repetir o mesmo padrão para Grau, Serie, Turno, InstituicaoEnsino e Curso
```

---

## Padrão de query hook (seguir exatamente este modelo)

```typescript
// src/features/instituicao/instituicaoQueries.ts
'use client';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useToast } from '@/components/ui/use-toast';
import { listarEnsinos, criarEnsino, atualizarEnsino, deletarEnsino } from './instituicaoService';
import type { EnsinoDTO } from './types';

export function useEnsinos() {
  return useQuery({ queryKey: ['ensinos'], queryFn: listarEnsinos });
}

export function useCriarEnsino() {
  const qc = useQueryClient();
  const { toast } = useToast();
  return useMutation({
    mutationFn: (dto: Omit<EnsinoDTO, 'idEnsino'>) => criarEnsino(dto),
    onSuccess: () => {
      toast({ title: 'Ensino criado com sucesso' });
      qc.invalidateQueries({ queryKey: ['ensinos'] });
    },
    onError: (e: Error) => toast({ title: e.message, variant: 'destructive' }),
  });
}

export function useAtualizarEnsino() {
  const qc = useQueryClient();
  const { toast } = useToast();
  return useMutation({
    mutationFn: ({ id, dto }: { id: number; dto: Omit<EnsinoDTO, 'idEnsino'> }) =>
      atualizarEnsino(id, dto),
    onSuccess: () => {
      toast({ title: 'Ensino atualizado com sucesso' });
      qc.invalidateQueries({ queryKey: ['ensinos'] });
    },
    onError: (e: Error) => toast({ title: e.message, variant: 'destructive' }),
  });
}

export function useDeletarEnsino() {
  const qc = useQueryClient();
  const { toast } = useToast();
  return useMutation({
    mutationFn: (id: number) => deletarEnsino(id),
    onSuccess: () => {
      toast({ title: 'Excluído com sucesso' });
      qc.invalidateQueries({ queryKey: ['ensinos'] });
    },
    onError: (e: Error) => toast({ title: e.message, variant: 'destructive' }),
  });
}
// Repetir o mesmo padrão para Grau, Serie, Turno, InstituicaoEnsino e Curso
```

---

## Validação Zod por recurso

```typescript
// Ensino / Grau / Série / Turno — schema simples
const schemaNome = z.object({
  nome: z.string().min(1, 'Nome é obrigatório').max(255),
});

// InstituicaoEnsino — campos opcionais
const schemaInstituicao = z.object({
  descricao: z.string().max(255).optional(),
  codigoEstadual: z.string().max(255).optional(),
});

// Curso — 3 FKs obrigatórias
const schemaCurso = z.object({
  idEnsino: z.coerce.number({ required_error: 'Ensino é obrigatório' }),
  idGrau:   z.coerce.number({ required_error: 'Grau é obrigatório' }),
  idSerie:  z.coerce.number({ required_error: 'Série é obrigatória' }),
});
```

---

## Estados de loading e erro

- Durante carregamento: usar `Skeleton` do shadcn/ui na área da tabela
- Em caso de erro de listagem: exibir mensagem inline "Não foi possível carregar os dados." com botão Tentar novamente
- Botões de submit: desabilitar com `disabled={isPending}` enquanto a mutation estiver em andamento
- Após sucesso de criar/editar: fechar o Dialog automaticamente (`setOpen(false)`)
- Após sucesso de excluir: fechar o AlertDialog automaticamente

---

## O que NÃO fazer

- Não criar rotas separadas para cada recurso (ex: `/admin/ensinos`, `/admin/graus`) — tudo em uma única página com Tabs
- Não usar `fetch` diretamente — sempre usar o `axiosInstance` configurado
- Não criar componentes de tabela/dialog/form do zero — usar shadcn/ui
- Não ignorar o campo `error` da `ApiResponse` — é onde vem a mensagem de negócio do backend
- Não duplicar a lógica de service nos query hooks — o hook chama o service, o service chama a API
- Não usar `router.push` para recarregar após mutação — usar `queryClient.invalidateQueries`

---

## Checklist de entrega

- [ ] `src/features/instituicao/types.ts` — 6 DTOs TypeScript
- [ ] `src/features/instituicao/instituicaoService.ts` — funções de API para todos os 6 recursos
- [ ] `src/features/instituicao/instituicaoQueries.ts` — hooks TanStack Query para todos os 6 recursos
- [ ] `src/app/(dashboard)/admin/layout.tsx` — guard de role ADMINISTRADOR (se ainda não existir)
- [ ] `src/app/(dashboard)/admin/instituicao/page.tsx` — página com Tabs
- [ ] Aba InstituicaoEnsino: Table + Dialog (descricao + codigoEstadual) + AlertDialog exclusão
- [ ] Aba Ensino: Table + Dialog (nome) + AlertDialog exclusão
- [ ] Aba Grau: Table + Dialog (nome) + AlertDialog exclusão
- [ ] Aba Série: Table + Dialog (nome) + AlertDialog exclusão
- [ ] Aba Turno: Table + Dialog (nome) + AlertDialog exclusão
- [ ] Aba Curso: Table (com nomes resolvidos) + Dialog (3 Selects) + AlertDialog exclusão
- [ ] Botões de escrita ocultos para COORDENADOR (visível apenas para ADMINISTRADOR)
- [ ] Loading states com Skeleton
- [ ] Erros de API exibidos via toast
