# Front-end – Gerenciamento de Artistas e Álbuns

Aplicação Front-end desenvolvida em React + TypeScript para consumo da API do projeto Gerenciamento de Artistas e Álbuns, criado como Prova Prática Full Stack Sênior – Java + React conforme edital da SEPLAG – Governo do Estado de Mato Grosso.

A interface permite autenticação, listagem de artistas, visualização de álbuns, cadastro de novos álbuns e upload de capas.

## Objetivo

Fornecer uma interface web moderna, responsiva e intuitiva para:
- Autenticação de usuários
- Listagem de artistas
- Visualização de álbuns por artista
- Cadastro de álbuns
- Upload de capas
- Consumo de notificações via WebSocket

## Tecnologias Utilizadas
- React 18
- TypeScript
- Vite
- React Router DOM
- Axios
- CSS Modules / CSS puro
- WebSocket (SockJS + STOMP)
- ESLint

## Arquitetura
Estrutura baseada em módulos e separação de responsabilidades:
```text
src
├── app
│   └── routes
├── components
├── modules
│   ├── artistas
│   └── albuns
├── services
└── styles

```
## Integração com Back-end
```arduino
http://localhost:8080
```
Endpoints principais:

- POST /api/v1/auth/login
- GET /api/v1/artistas
- GET /api/v1/albuns?artistaId=
- POST /api/v1/albuns
- POST /api/v1/albuns/{id}/capas

## Autenticação
- Login via JWT
- Access Token armazenado no localStorage
- Token enviado automaticamente no header:
```text
Authorization: Bearer <token>
```

## Upload de Capas
- Upload múltiplo de imagens
- Pré-visualização automática
- Capas exibidas na tela de detalhe do artista

## Notificações em Tempo Real
O front conecta ao WebSocket:
```bash
http://localhost:8080/ws
```
Canal:
```bash
/topic/albuns
```
Sempre que um álbum é criado, o usuário recebe notificação automática.

## Executar o Projeto
Pré-requisitos
- Node.js 18+
- NPM ou Yarn
- Back-end em execução
- 
 Instalação
```bash
npm install
```
Executar
```bash
npm run dev
```
Acessar
```bash
http://localhost:5173
```
## Variáveis de Ambiente (opcional)
Arquivo .env:
```env
VITE_API_URL=http://localhost:8080
```
## Telas Principais
- Login
- Listagem de artistas
- Detalhe do artista com álbuns
- Cadastro de álbum

## Boas Práticas Aplicadas
- Componentização
- Tipagem forte com TypeScript
- Separação de módulos
- Reutilização de serviços
- Tratamento de erros
- Organização por responsabilidade

## Observações
O frontend utiliza variáveis de ambiente para definição dinâmica da URL da API, permitindo execução local e em containers sem alteração de código.

O rate limit está configurado para 10 requisições por minuto por usuário. Durante testes locais, múltiplos refreshes consecutivos podem retornar 429 (Too Many Requests), comportamento esperado conforme configuração.

Este front-end foi desenvolvido com foco em clareza, simplicidade e aderência aos requisitos funcionais do projeto, priorizando integração correta com o back-end e boa experiência de uso.