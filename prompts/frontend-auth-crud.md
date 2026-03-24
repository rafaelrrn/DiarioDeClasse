# Prompt — Frontend: Domínio Auth (Login, Logout, Registro, Perfil)

## Contexto

Este projeto usa **Next.js 14 App Router** com **Tailwind CSS + shadcn/ui**, seguindo as convenções do `INSTRUCTIONS_FRONTEND.md`.
O backend é Spring Boot com autenticação baseada em **cookie HttpOnly** chamado `auth_token`.

Implemente o domínio de autenticação conforme descrito abaixo. Leia o `INSTRUCTIONS_FRONTEND.md` antes de começar.

---

## Análise do Backend

### Endpoints disponíveis

| Método | Rota              | Auth | Descrição                                           |
|--------|-------------------|------|-----------------------------------------------------|
| POST   | /v1/auth/register | Não  | Cria novo usuário (nome, email, senha, role)        |
| POST   | /v1/auth/login    | Não  | Autentica e define cookie `auth_token` (HttpOnly)   |
| POST   | /v1/auth/logout   | Não  | Zera o cookie (maxAge=0); token não é invalidado    |
| GET    | /v1/auth/me       | Sim  | Retorna dados do usuário autenticado via cookie     |

### DTOs relevantes

**Login request** (body direto — não há LoginRequest DTO):
```json
{ "email": "usuario@escola.com", "senha": "minhasenha" }
```

**Register request** (body direto — entidade User):
```json
{ "nome": "Rafael", "email": "rafael@escola.com", "senha": "senha123", "role": "ADMINISTRADOR" }
```

**UserMeResponse** (retorno de `GET /auth/me`):
```json
{
  "idUser": 1,
  "email": "rafael@escola.com",
  "nome": "Rafael",
  "role": "ADMINISTRADOR"
}
```

**Role enum** (todos os valores possíveis):
```
ADMINISTRADOR | DIRETOR | COORDENADOR | PROFESSOR | RESPONSAVEL | ALUNO
```

**Hierarquia de roles** (importante para permissões):
```
ADMINISTRADOR > DIRETOR > COORDENADOR > PROFESSOR > RESPONSAVEL > ALUNO
```
O backend usa essa hierarquia no `@PreAuthorize`. O frontend usa `useRoles()` para gating de UI.

### Comportamento crítico do cookie

- **Cookie**: `auth_token` — HttpOnly, path `/`, maxAge=86400 (1 dia no cookie)
- **Token JWT**: expira em **1 hora** (independente do cookie durar 1 dia)
- Após 1 hora o cookie ainda existe, mas o token dentro dele está expirado → o backend retornará **401**
- **Não há refresh token** — ao receber 401, redirecionar para `/login`
- Axios **deve** usar `withCredentials: true` para enviar o cookie automaticamente

### CORS (dev)
Backend permite apenas `http://localhost:3000` com `allowCredentials: true`.

### Limitações da API
- **Não existe** `GET /v1/auth/users` para listar usuários
- **Não existe** `PUT /v1/auth/users/{id}` para editar usuários
- **Não existe** `DELETE /v1/auth/users/{id}`
- Registro é a única forma de criar usuários — sem restrição de role na rota (`/v1/auth/register` é pública)
- Na prática: o frontend deve restringir o formulário de registro à UI de admin

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

> **Nota**: coloque o interceptor de redirect apenas no lado do cliente. No servidor (Server Components), o redirect é feito via middleware do Next.js.

---

### 2. Store de autenticação (`store/authStore.ts`)

```typescript
import { create } from 'zustand';

interface AuthUser {
  idUser: number;
  email: string;
  nome: string;
  role: string;
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

  // true se o usuário tem nível >= target (hierarquia)
  const atLeast = (target: string) => {
    const userIdx = HIERARCHY.indexOf(role);
    const targetIdx = HIERARCHY.indexOf(target);
    return userIdx >= targetIdx;
  };

  return { role, is, hasAny, atLeast };
}
```

---

### 4. Middleware do Next.js (`middleware.ts` — raiz do projeto)

```typescript
import { NextRequest, NextResponse } from 'next/server';

const PUBLIC_PATHS = ['/login', '/register'];

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

Componente Client que roda `GET /auth/me` uma vez ao carregar o app e popula o Zustand store.

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
      .catch(() => {/* middleware já redireciona se 401 */});
  }, []);

  return <>{children}</>;
}
```

Adicione `<AuthProvider>` no `app/layout.tsx` (fora do `(auth)` route group).

---

### 6. Página de Login (`app/(auth)/login/page.tsx`)

**Comportamento:**
- Campos: `email` (Input type="email") + `senha` (Input type="password")
- Submit: `POST /v1/auth/login`
- Sucesso: backend define o cookie automaticamente → chamar `GET /auth/me` → popular store → `router.push('/dashboard')`
- Erro: exibir mensagem da resposta (`error` ou `message` do `ApiResponse`)
- Layout: centrado na tela, Card com logo/título da escola
- Sem botão de registro visível (o registro é feito pelo admin)

**Validação Zod:**
```typescript
const schema = z.object({
  email: z.string().email('E-mail inválido'),
  senha: z.string().min(1, 'Senha obrigatória'),
});
```

**Fluxo após login:**
```typescript
await api.post('/v1/auth/login', data);
const me = await api.get('/v1/auth/me');
setUser(me.data.data);
router.push('/dashboard');
```

---

### 7. Logout

**Onde colocar:** botão no sidebar/header do layout do dashboard.

**Fluxo:**
```typescript
await api.post('/v1/auth/logout');
clear(); // limpa Zustand
router.push('/login');
```

Usar `AlertDialog` do shadcn para confirmar antes de deslogar.

---

### 8. Página de Registro (Admin) (`app/(dashboard)/admin/usuarios/novo/page.tsx`)

> **Acesso restrito**: apenas ADMINISTRADOR.

Como não existe endpoint de listagem, implemente apenas o formulário de criação.
Use um `Card` simples com os campos:

| Campo | Tipo | Validação |
|-------|------|-----------|
| nome  | Input text | obrigatório |
| email | Input email | obrigatório, formato e-mail |
| senha | Input password | mín. 6 caracteres |
| role  | Select | obrigatório, valores do enum Role |

**Opções do Select de role** (exibir label amigável):
```typescript
const ROLE_OPTIONS = [
  { value: 'ADMINISTRADOR', label: 'Administrador' },
  { value: 'DIRETOR', label: 'Diretor' },
  { value: 'COORDENADOR', label: 'Coordenador' },
  { value: 'PROFESSOR', label: 'Professor' },
  { value: 'RESPONSAVEL', label: 'Responsável' },
  { value: 'ALUNO', label: 'Aluno' },
];
```

**Validação Zod:**
```typescript
const schema = z.object({
  nome: z.string().min(1, 'Nome obrigatório'),
  email: z.string().email('E-mail inválido'),
  senha: z.string().min(6, 'Mínimo 6 caracteres'),
  role: z.enum(['ADMINISTRADOR','DIRETOR','COORDENADOR','PROFESSOR','RESPONSAVEL','ALUNO']),
});
```

Submit: `POST /v1/auth/register` → toast de sucesso → redirect para `/admin/usuarios`.

**Guard no layout:**
```typescript
// app/(dashboard)/admin/usuarios/layout.tsx
import { cookies } from 'next/headers';
// ou use useRoles() no client component
```

No `page.tsx` (Client Component):
```typescript
const { atLeast } = useRoles();
if (!atLeast('ADMINISTRADOR')) return <p>Acesso negado.</p>;
```

---

### 9. Página de Perfil (`app/(dashboard)/perfil/page.tsx`)

Exibe os dados do usuário logado. **Somente leitura** (não há PUT /auth/me).

```typescript
'use client';
const { user } = useAuthStore();
```

Layout:
```
Card
  Avatar com inicial do nome
  nome (título)
  email
  Badge com role (mesmo estilo de RoleBadge)
```

**RoleBadge** — componente para exibir a role com cor:
```typescript
const ROLE_COLORS: Record<string, string> = {
  ADMINISTRADOR: 'bg-red-100 text-red-800',
  DIRETOR: 'bg-purple-100 text-purple-800',
  COORDENADOR: 'bg-blue-100 text-blue-800',
  PROFESSOR: 'bg-green-100 text-green-800',
  RESPONSAVEL: 'bg-yellow-100 text-yellow-800',
  ALUNO: 'bg-gray-100 text-gray-800',
};

export function RoleBadge({ role }: { role: string }) {
  return (
    <span className={`px-2 py-0.5 rounded text-xs font-medium ${ROLE_COLORS[role] ?? ''}`}>
      {role}
    </span>
  );
}
```

---

## Estrutura de arquivos

```
app/
  (auth)/
    login/
      page.tsx          ← formulário de login
  (dashboard)/
    admin/
      usuarios/
        novo/
          page.tsx      ← formulário de registro (ADMIN only)
    perfil/
      page.tsx          ← perfil do usuário logado
middleware.ts           ← edge auth guard
lib/
  axios.ts              ← instância Axios com withCredentials + interceptor 401
store/
  authStore.ts          ← Zustand: user autenticado
hooks/
  useRoles.ts           ← is(), hasAny(), atLeast()
providers/
  AuthProvider.tsx      ← hidrata store com GET /auth/me no boot
```

---

## Comportamentos importantes

### Token expira em 1 hora (não há refresh)

O cookie dura 1 dia, mas o JWT dentro dele expira em 1 hora. Quando isso acontece:
- Qualquer chamada à API retorna 401
- O interceptor do Axios redireciona para `/login`
- O middleware do Next.js ainda vê o cookie (não expirado) mas o backend rejeita — portanto o interceptor é a linha de defesa

**Não** implemente lógica de refresh — o backend não oferece esse endpoint.

### Segurança no registro

O endpoint `POST /v1/auth/register` é público no backend. Portanto:
- Não exponha o link de registro em nenhum lugar público da UI
- O acesso ao formulário `/admin/usuarios/novo` deve ser protegido por role no cliente

### withCredentials é obrigatório

Sem `withCredentials: true` no Axios, o browser não envia o cookie para o backend e todas as requisições autenticadas falham com 401. Configure globalmente na instância `api`, não por chamada.

### Logout é stateless

O backend não invalida o token no servidor — apenas zera o cookie no browser. Isso significa que se alguém capturar o token antes do logout, ele continuará válido até expirar (1 hora). É uma limitação do design atual — não implemente lógica de blacklist no frontend.

---

## Checklist de implementação

- [ ] `lib/axios.ts` — instância com `withCredentials: true` + interceptor 401
- [ ] `store/authStore.ts` — Zustand com `user`, `setUser`, `clear`
- [ ] `hooks/useRoles.ts` — `is()`, `hasAny()`, `atLeast()`
- [ ] `providers/AuthProvider.tsx` — GET /auth/me no boot
- [ ] `middleware.ts` — redireciona não-autenticado e autenticado em rota pública
- [ ] `/login` — formulário com Zod, POST /auth/login, hidratação do store
- [ ] Logout no header/sidebar — AlertDialog + POST /auth/logout + clear + redirect
- [ ] `/admin/usuarios/novo` — formulário de registro com Select de role (ADMIN only)
- [ ] `/perfil` — dados do usuário com RoleBadge
- [ ] `RoleBadge` — componente reutilizável com cores por role
