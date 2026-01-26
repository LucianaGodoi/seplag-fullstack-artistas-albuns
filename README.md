# Projeto Full Stack – Gerenciamento de Artistas e Álbuns

Projeto desenvolvido como **Prova Prática Full Stack Sênior – Java + React**, conforme edital da **SEPLAG – Governo do Estado de Mato Grosso**.

A aplicação permite o gerenciamento de **artistas** e seus **álbuns**, incluindo autenticação segura, upload de capas, paginação e controle de acesso.

---

## 🎯 Objetivo

Implementar uma solução **Full Stack** que possibilite:

- Cadastro e consulta de artistas
- Cadastro e consulta de álbuns
- Upload de múltiplas capas de álbuns
- Autenticação segura com JWT
- Arquitetura moderna, escalável e bem documentada

---

## 🛠️ Tecnologias Utilizadas

### Back-end
- Java 21
- Spring Boot
- Spring Security
- JWT (Access + Refresh Token)
- PostgreSQL
- Flyway
- MinIO (S3)
- Swagger / OpenAPI
- Bucket4j (Rate Limit)
- Docker / Docker Compose

### Front-end (planejado)
- React
- TypeScript
- Arquitetura Facade
- Gerenciamento de estado com BehaviorSubject
- Tailwind CSS

---

## 🧱 Arquitetura

A aplicação é composta por containers independentes:

- **API** (Spring Boot)
- **Banco de Dados** (PostgreSQL)
- **Storage** (MinIO – S3)
- **Front-end** (React – planejado)

Todos os serviços são orquestrados via **Docker Compose**.

---

## 📦 Estrutura do Projeto (Back-end)
```md
src/main/java
├── api
│ ├── controller
│ └── dto
├── config
├── domain
│ ├── entity
│ ├── repository
│ └── service
├── mapper
└── util
```
---

## 🔐 Segurança

- Autenticação **JWT Stateless**
- Access Token com expiração de **5 minutos**
- Refresh Token persistido em banco
- Rotação e revogação de Refresh Token
- Rate Limit de **10 requisições por minuto por usuário**
- CORS configurado por ambiente

---

## 🗄️ Banco de Dados

- PostgreSQL
- Versionamento com **Flyway**
- Migrations para:
    - Estrutura inicial
    - Usuário administrador
    - Tokens de refresh
    - Relacionamentos entre artistas, álbuns e imagens

---

## 🖼️ Upload de Imagens

- Upload de **uma ou mais capas por álbum**
- Armazenamento no **MinIO**
- Recuperação via **URL pré-assinada**
- Bucket utilizado: `album-capas`

---
```md
O upload é realizado via endpoint multipart/form-data, permitindo múltiplos arquivos no mesmo request.
```

## 📄 Documentação da API

- Swagger disponível em: http://localhost:8080/swagger-ui.html


---


## 🧪 Teste Rápido (Login)

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username": "admin",
  "password": "admin123"
}
```
```md
Credenciais padrão:

- Usuário: admin
- Senha: admin123
```
## 🚀 Como Executar Localmente

### Pré-requisitos
- Docker
- Docker Compose

### Passos

1. **Clonar o repositório**
```bash
git clone https://github.com/LucianaGodoi/seplag-fullstack-artistas-albuns
cd seplag-fullstack-artistas-albuns
```
2. **Subir os containers**
```bash
docker compose up -d --build
```
### Acessar os serviços
- API: http://localhost:8080

- Swagger (OpenAPI): http://localhost:8080/swagger-ui.html

- MinIO Console: http://localhost:9101

O usuário administrador (admin) é criado automaticamente via Flyway.

---

## 📌 Observações Finais

Este projeto foi desenvolvido priorizando boas práticas de arquitetura, segurança, organização de código e escalabilidade, conforme exigido no edital.
