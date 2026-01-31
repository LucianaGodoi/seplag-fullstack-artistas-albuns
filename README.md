# Projeto Full Stack – Gerenciamento de Artistas e Álbuns

Projeto desenvolvido como **Prova Prática Full Stack Sênior – Java + React**, conforme edital da **SEPLAG – Governo do Estado de Mato Grosso**.

A aplicação permite o gerenciamento de **artistas**, **álbuns** e **capas**, incluindo autenticação segura, upload de imagens, paginação e notificação em tempo real.

---

##  Objetivo

Implementar uma solução **Full Stack** que possibilite:

- Cadastro e consulta de artistas
- Cadastro e consulta de álbuns
- Upload de múltiplas capas de álbuns
- Autenticação segura com JWT
- Arquitetura moderna, escalável e bem documentada

---

##  Tecnologias Utilizadas

### Back-end
- Java 21
- Spring Boot 3
- Spring Security
- JWT (Access + Refresh Token)
- PostgreSQL
- Flyway
- MinIO (S3 compatível)
- Swagger / OpenAPI
- Bucket4j (Rate Limit)
- Docker / Docker Compose

### Front-end (planejado)
- React
- TypeScript
- Vite
- Arquitetura Facade
- Tailwind CSS

---

##  Arquitetura

A aplicação é composta por containers independentes:

- **API** (Spring Boot)
- **Banco de Dados** (PostgreSQL)
- **Storage** (MinIO)
- **Front-end** (React)

Todos os serviços são orquestrados via **Docker Compose**.

---

##  Estrutura do Projeto (Back-end)
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

##  Segurança

- Autenticação **JWT Stateless**
- Access Token com expiração de **5 minutos**
- Refresh Token persistido em banco
- Rotação e revogação de Refresh Token
- Rate Limit de **10 requisições/minuto por usuário**
- CORS configurado por ambiente

Perfis previstos:

- ADMIN: criação, edição e upload de capas
- USER: consultas

Durante o desenvolvimento algumas rotas estão liberadas para facilitar testes, mas o projeto já está preparado para restrição por perfil.

---

##  Banco de Dados

- PostgreSQL
- Versionamento com **Flyway**
- Migrations para:
    - Estrutura inicial
    - Usuário administrador
    - Tokens de refresh
    - Relacionamentos entre artistas, álbuns e imagens

---

## Upload de Imagens

- Upload de **uma ou mais capas por álbum**
- Armazenamento no **MinIO**
- Recuperação via **URL pré-assinada**
- Bucket utilizado: `album-capas`

O upload é realizado via:
```md
multipart/form-data
```

##  Documentação da API

- Swagger disponível em: 
```md
http://localhost:8080/swagger-ui.html
```

---

##  Teste Rápido (Login)

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
##  Como Executar Localmente

### Pré-requisitos
- Docker
- Docker Compose

### Passos

1. **Clonar o repositório**
```bash
git clone https://github.com/LucianaGodoi/seplag-fullstack-artistas-albuns
cd seplag-fullstack-artistas-albuns
docker compose up -d --build
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

## WebSocket – Notificação de Novo Álbum
Sempre que um novo álbum é criado, todos os clientes conectados recebem notificação em tempo real.

-  Endpoint WebSocket
```md
/ws
```
- Canal
```md
/topic/albuns
```
- O disparo ocorre em:
```md
AlbumService.criar()
```
- Teste Rápido no Navegador: 
- Cole no console:
```js
var s1=document.createElement("script");
s1.src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js";
document.head.appendChild(s1);

s1.onload=()=> {
  var s2=document.createElement("script");
  s2.src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js";
  document.head.appendChild(s2);

  s2.onload=()=> {
    const socket=new SockJS("http://localhost:8080/ws");
    const client=Stomp.over(socket);

    client.connect({},()=>{
      console.log("Conectado ao WebSocket");

      client.subscribe("/topic/albuns",msg=>{
        console.log("Notificação recebida:");
        console.log(JSON.parse(msg.body));
      });
    });
  };
};

```
### Teste de Autenticação

1. Realizar login com usuário admin
2. Validar tokens no LocalStorage
3. Remover manualmente accessToken
4. Recarregar página
5. Sistema realiza refresh automático
6. Novo token é gerado sem logout

Fluxo validado com sucesso.

##  Observações Finais

Este projeto foi desenvolvido priorizando:
- Boas práticas de arquitetura
- Organização em camadas
- Segurança
- Escalabilidade
- Clareza de documentação

Atendendo integralmente aos requisitos do edital.
