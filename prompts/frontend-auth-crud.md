# Prompt — Frontend: Domínio Auth + Gerenciamento de Usuários

## Contexto

Este projeto usa **Next.js 14 App Router** com **Tailwind CSS + shadcn/ui**, seguindo as convenções do `INSTRUCTIONS_FRONTEND.md`.
O backend é Spring Boot com autenticação baseada em **cookie HttpOnly** chamado `auth_token`.

Implemente o domínio de autenticação e gerenciamento de usuários conforme descrito abaixo. Leia o `INSTRUCTIONS_FRONTEND.md` antes de começar.

---

## Análise do Backend

### Endpoints disponíveis

#### Auth (`/v1/auth`) — rotas públicas

| Método | Rota              | Auth | Descrição                                                    |
|--------|-------------------|------|--------------------------------------------------------------|
| POST   | /v1/auth/register | Não  | Cria novo usuário (nome, email, senha, role, idPessoa?)      |
| POST   | /v1/auth/login    | Não  | Autentica e define cookie `auth_token` (HttpOnly)            |
| POST   | /v1/auth/logout   | Não  | Zera o cookie (maxAge=0); token não é invalidado no servidor |
| GET    | /v1/auth/me       | Sim  | Retorna dados do usuário autenticado via cookie              |

#### Users (`/v1/users`) — apenas ADMINISTRADOR

| Método | Rota            | Descrição                                                      |
|--------|-----------------|----------------------------------------------------------------|
| GET    | /v1/users       | Lista todos os usuários                                        |
| GET    | /v1/users/{id}  | Busca usuário por ID                                           |
| PUT    | /v1/users/{id}  | Atualiza nome, email, role, senha (opcional) e vínculo Pessoa  |

### DTOs relevantes

**RegisterRequest** — `POST /v1/auth/register`:
```json
{
  "nome": "Rafael",
  "email": "rafael@escola.com",
  "senha": "senha123",
  "role": "PROFESSOR",
  "idPessoa": 7
}
```
> `idPessoa` é **opcional** (pode ser omitido ou `null`). Obrigatório para Professor, Aluno e Responsável; dispensável para administradores técnicos.

**Login request** — `POST /v1/auth/login`:
```json
{ "email": "usuario@escola.com", "senha": "minhasenha" }
```

**UserMeResponse** — retorno de `GET /auth/me` e de todos os endpoints de `/v1/users`:
```json
{
  "idUser": 1,
  "email": "rafael@escola.com",
  "nome": "Rafael",
  "role": "PROFESSOR",
  "idPessoa": 7
}
```
> `idPessoa` é `null` para usuários sem vínculo pedagógico.

**UserUpdateRequest** — `PUT /v1/users/{id}`:
```json
{
  "nome": "Rafael Atualizado",
  "email": "rafael@escola.com",
  "senha": "",
  "role": "COORDENADOR",
  "idPessoa": 12
}
```
> Se `senha` for vazia ou `null`, a senha atual é **mantida**.
> Para **desvincular** Pessoa, enviar `"idPessoa": null`.
> Para **vincular**, enviar o ID da Pessoa desejada.

**Role enum** (todos os valores possíveis):
```
ADMINISTRADOR | DIRETOR | COORDENADOR | PROFESSOR | RESPONSAVEL | ALUNO
```

**Hierarquia de roles** (importante para permissões):
```
ADMINISTRADOR > DIRETOR > COORDENADOR > PROFESSOR > RESPONSAVEL > ALUNO
```

### Comportamento crítico do cookie

- **Cookie**: `auth_token` — HttpOnly, path `/`, maxAge=86400 (1 dia no cookie)
- **Token JWT**: expira em **1 hora** (independente do cookie durar 1 dia)
- Após 1 hora, o backend retorna **401** — redirecionar para `/login`
- **Não há refresh token** — o usuário deve fazer login novamente
- Axios **deve** usar `withCredentials: true` para enviar o cookie automaticamente

---

## O que implementar

### 1. Configuração do Axios (`lib/axios.ts`)

```typescript
import axios from 'axios';

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  withCredentials: true, // OBRIGATÓRIO — envia o cookie auth_token
});

// Interceptor: redireciona para /login em qualquer 401
api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

### 2. Store de autenticação (`store/authStore.ts`)

Incluir `idPessoa` para que o frontend saiba qual Pessoa corresponde ao usuário logado (útil para carregar o boletim, frequência etc. do próprio usuário).

```typescript
import { create } from 'zustand';

interface AuthUser {
  idUser: number;
  email: string;
  nome: string;
  role: string;
  idPessoa: number | null; // null = sem vínculo pedagógico
}

interface AuthStore {
  user: AuthUser | null;
  setUser: (user: AuthUser | null) => void;
  clear: () => void;
}

export const useAuthStore = create<AuthStore>((set) => ({
  user: null,
  setUser: (user) => set({ user }),
  clear: () => set({ user: null }),
}));
```

---

### 3. Hook `useRoles()` (`hooks/useRoles.ts`)

```typescript
import { useAuthStore } from '@/store/authStore';

const HIERARCHY = ['ALUNO', 'RESPONSAVEL', 'PROFESSOR', 'COORDENADOR', 'DIRETOR', 'ADMINISTRADOR'];

export function useRoles() {
  const role = useAuthStore((s) => s.user?.role ?? '');

  const is = (target: string) => role === target;
  const hasAny = (...roles: string[]) => roles.includes(role);
  const atLeast = (target: string) => {
    return HIERARCHY.indexOf(role) >= HIERARCHY.indexOf(target);
  };

  return { role, is, hasAny, atLeast };
}
```

---

### 4. Middleware do Next.js (`middleware.ts` — raiz do projeto)

```typescript
import { NextRequest, NextResponse } from 'next/server';

const PUBLIC_PATHS = ['/login'];

export function middleware(request: NextRequest) {
  const token = request.cookies.get('auth_token');
  const path = request.nextUrl.pathname;
  const isPublic = PUBLIC_PATHS.some((p) => path.startsWith(p));

  if (!isPublic && !token) {
    return NextResponse.redirect(new URL('/login', request.url));
  }
  if (isPublic && token) {
    return NextResponse.redirect(new URL('/dashboard', request.url));
  }
  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!_next/static|_next/image|favicon.ico|api).*)'],
};
```

---

### 5. Inicialização do usuário (`providers/AuthProvider.tsx`)

```typescript
'use client';
import { useEffect } from 'react';
import { useAuthStore } from '@/store/authStore';
import { api } from '@/lib/axios';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const setUser = useAuthStore((s) => s.setUser);

  useEffect(() => {
    api.get('/v1/auth/me')
      .then((res) => setUser(res.data.data))
      .catch(() => {/* middleware redireciona se 401 */});
  }, []);

  return <>{children}</>;
}
```

Adicione `<AuthProvider>` no `app/layout.tsx`.

---

### 6. Página de Login (`app/(auth)/login/page.tsx`)

**Comportamento:**
- Campos: `email` + `senha`
- Submit: `POST /v1/auth/login` → `GET /auth/me` → popular store → `router.push('/dashboard')`
- Erro: exibir `error` ou `message` da `ApiResponse`
- Layout: Card centralizado com logo/título da escola

```typescript
const schema = z.object({
  email: z.string().email('E-mail inválido'),
  senha: z.string().min(1, 'Senha obrigatória'),
});
```

---

### 7. Logout (no header/sidebar do dashboard)

```typescript
await api.post('/v1/auth/logout');
clear(); // limpa Zustand
router.push('/login');
```

Usar `AlertDialog` do shadcn para confirmar antes de deslogar.

---

### 8. Página de listagem de usuários (`app/(dashboard)/admin/usuarios/page.tsx`)

> **Acesso restrito**: apenas ADMINISTRADOR.

Exibe `GET /v1/users` em uma `Table` do shadcn com as colunas:

| Coluna       | Dado                                    |
|--------------|-----------------------------------------|
| Nome         | `nome`                                  |
| E-mail       | `email`                                 |
| Role         | `RoleBadge` (componente com cor)        |
| Pessoa       | `idPessoa` — exibir "Vinculada" ou badge "Sem vínculo" em cinza |
| Ações        | Botão "Editar" → `/admin/usuarios/{id}` |

Botão "Novo usuário" no topo → `/admin/usuarios/novo`.

**Query key TanStack Query**: `['users']`

---

### 9. Página de criação (`app/(dashboard)/admin/usuarios/novo/page.tsx`)

Formulário com os campos:

| Campo    | Tipo           | Validação                              |
|----------|----------------|----------------------------------------|
| nome     | Input text     | obrigatório                            |
| email    | Input email    | obrigatório, formato e-mail            |
| senha    | Input password | obrigatório, mín. 6 caracteres         |
| role     | Select         | obrigatório                            |
| idPessoa | Select         | opcional — carregado de `GET /v1/pessoas` |

**Select de Pessoa:**
- Carregar `GET /v1/pessoas` para preencher as opções
- Exibir `nome` da Pessoa
- Opção vazia "— Sem vínculo —" para deixar `null`
- Usar `z.coerce.number().optional().nullable()` no Zod para o campo

```typescript
const schema = z.object({
  nome: z.string().min(1, 'Nome obrigatório'),
  email: z.string().email('E-mail inválido'),
  senha: z.string().min(6, 'Mínimo 6 caracteres'),
  role: z.enum(['ADMINISTRADOR','DIRETOR','COORDENADOR','PROFESSOR','RESPONSAVEL','ALUNO']),
  idPessoa: z.coerce.number().optional().nullable(),
});
```

Submit: `POST /v1/auth/register` → invalidar query `['users']` → toast de sucesso → `router.push('/admin/usuarios')`.

---

### 10. Página de edição (`app/(dashboard)/admin/usuarios/[id]/page.tsx`)

Carrega `GET /v1/users/{id}` e preenche o formulário.

Campos iguais ao de criação, com as seguintes diferenças:
- **Senha**: campo opcional — se vazio, a senha não é alterada. Exibir label `"Nova senha (deixe vazio para manter)"`.
- **idPessoa**: exibe qual Pessoa está atualmente vinculada; permite trocar ou limpar (opção "— Remover vínculo —" que envia `null`).

```typescript
const schema = z.object({
  nome: z.string().min(1, 'Nome obrigatório'),
  email: z.string().email('E-mail inválido'),
  senha: z.string().optional().or(z.literal('')),
  role: z.enum(['ADMINISTRADOR','DIRETOR','COORDENADOR','PROFESSOR','RESPONSAVEL','ALUNO']),
  idPessoa: z.coerce.number().optional().nullable(),
});
```

Submit: `PUT /v1/users/{id}` → invalidar `['users']` e `['users', id]` → toast de sucesso.

**Aviso de segurança** — exibir um `Alert` informativo quando o usuário editar a própria conta:
```typescript
const { user } = useAuthStore();
const isSelf = user?.idUser === Number(params.id);
// se isSelf: mostrar Alert "Você está editando sua própria conta. Alterações de role têm efeito imediato."
```

---

### 11. `RoleBadge` — componente reutilizável

```typescript
const ROLE_COLORS: Record<string, string> = {
  ADMINISTRADOR: 'bg-red-100 text-red-800',
  DIRETOR:       'bg-purple-100 text-purple-800',
  COORDENADOR:   'bg-blue-100 text-blue-800',
  PROFESSOR:     'bg-green-100 text-green-800',
  RESPONSAVEL:   'bg-yellow-100 text-yellow-800',
  ALUNO:         'bg-gray-100 text-gray-800',
};

const ROLE_LABELS: Record<string, string> = {
  ADMINISTRADOR: 'Administrador',
  DIRETOR:       'Diretor',
  COORDENADOR:   'Coordenador',
  PROFESSOR:     'Professor',
  RESPONSAVEL:   'Responsável',
  ALUNO:         'Aluno',
};

export function RoleBadge({ role }: { role: string }) {
  return (
    <span className={`px-2 py-0.5 rounded text-xs font-medium ${ROLE_COLORS[role] ?? 'bg-gray-100 text-gray-800'}`}>
      {ROLE_LABELS[role] ?? role}
    </span>
  );
}
```

---

### 12. Página de Perfil (`app/(dashboard)/perfil/page.tsx`)

Exibe os dados do usuário logado via `useAuthStore()`. Somente leitura.

Layout:
```
Card
  Avatar com inicial do nome
  nome (título h2)
  email (texto secundário)
  RoleBadge com a role
  Se idPessoa não for null: link "Ver meu perfil pedagógico" → /pessoas/{idPessoa}
```

---

## Estrutura de arquivos

```
app/
  (auth)/
    login/
      page.tsx                    ← formulário de login
  (dashboard)/
    admin/
      usuarios/
        page.tsx                  ← listagem de usuários (ADMIN only)
        novo/
          page.tsx                ← formulário de criação
        [id]/
          page.tsx                ← formulário de edição + vínculo Pessoa
    perfil/
      page.tsx                    ← perfil do usuário logado
middleware.ts                     ← edge auth guard
lib/
  axios.ts                        ← Axios com withCredentials + interceptor 401
store/
  authStore.ts                    ← Zustand: user com idPessoa
hooks/
  useRoles.ts                     ← is(), hasAny(), atLeast()
providers/
  AuthProvider.tsx                ← hidrata store com GET /auth/me no boot
components/
  RoleBadge.tsx                   ← badge com cor por role
```

---

## Comportamentos importantes

### idPessoa no store habilita navegação contextual

Como `useAuthStore` agora expõe `idPessoa`, qualquer componente pode redirecionar o usuário logado para sua própria página pedagógica:
```typescript
const { user } = useAuthStore();
if (user?.idPessoa) router.push(`/alunos/${user.idPessoa}/boletim`);
```
Use isso na página de perfil e no dashboard para criar atalhos personalizados por role.

### Senha vazia = sem alteração

O backend verifica `senha != null && !senha.isBlank()`. Portanto, no `PUT /v1/users/{id}`, enviar `"senha": ""` ou omitir o campo mantém a senha atual. O frontend deve deixar o campo vazio por padrão na edição.

### Desvincular Pessoa

Enviar `"idPessoa": null` no `PUT /v1/users/{id}` remove o vínculo. A Pessoa **não é deletada** — apenas a FK é zerada em `users`.

### E-mail duplicado retorna 422

O backend lança `BusinessException` se o e-mail já existir. O interceptor do Axios captura o erro e o frontend deve exibir a mensagem da `ApiResponse.error`.

### Segurança no registro

`POST /v1/auth/register` é público no backend. Não exponha o link de registro em lugar algum da UI pública. O acesso ao formulário em `/admin/usuarios/novo` é protegido pelo guard de role no cliente.

---

## Checklist de implementação

- [ ] `lib/axios.ts` — `withCredentials: true` + interceptor 401
- [ ] `store/authStore.ts` — incluir campo `idPessoa: number | null`
- [ ] `hooks/useRoles.ts` — `is()`, `hasAny()`, `atLeast()`
- [ ] `providers/AuthProvider.tsx` — GET /auth/me no boot
- [ ] `middleware.ts` — redireciona não-autenticado e autenticado em rota pública
- [ ] `components/RoleBadge.tsx` — badge com cor e label amigável por role
- [ ] `/login` — formulário com Zod, POST /auth/login, hidratação do store
- [ ] Logout no header/sidebar — AlertDialog + POST /auth/logout + clear + redirect
- [ ] `/admin/usuarios` — Table com listagem, coluna de vínculo, botão "Novo"
- [ ] `/admin/usuarios/novo` — formulário com Select de Pessoa opcional
- [ ] `/admin/usuarios/[id]` — edição com senha opcional + vínculo Pessoa + alerta "editando própria conta"
- [ ] `/perfil` — dados do usuário logado com link para perfil pedagógico se `idPessoa != null`
