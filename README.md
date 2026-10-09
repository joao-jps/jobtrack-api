# JobTrack API

API REST para acompanhar o andamento dos projetos de desenvolvimento. A ideia é reunir em um só lugar o objetivo de cada projeto, seu status, as matérias estudadas e as tecnologias usadas.

Este é um projeto prático pessoal. Além de organizar projetos, ele serve para estudar e aplicar conceitos de desenvolvimento de APIs com Java e Spring.

## O que já dá para fazer

- Criar, listar, consultar, atualizar e remover projetos.
- Atualizar o status de um projeto separadamente.
- Cadastrar, listar, atualizar e remover as stacks de cada projeto.
- Cadastrar, listar, atualizar e remover as matérias relacionadas a cada projeto.

O acompanhamento de candidaturas para programas de estágio é uma ideia futura. A tabela inicial já está prevista nas migrations do banco, mas os endpoints de candidaturas ainda não fazem parte da API.

## Tecnologias usadas

- **Java 17** — linguagem do projeto.
- **Spring Boot 4.1.1** — configuração e execução da aplicação.
- **Spring Web MVC** — criação das rotas REST.
- **Spring Data JPA** — acesso e gravação dos dados no banco.
- **PostgreSQL 16** — banco de dados.
- **Flyway** — criação e controle das mudanças no banco por migrations.
- **Springdoc OpenAPI** — geração da página de documentação da API.
- **Maven Wrapper** — build e execução do projeto sem precisar instalar o Maven manualmente.
- **Docker Compose** — execução local do PostgreSQL.

## O que estou praticando

- Separação do código em controllers, services, repositórios, modelos e DTOs.
- Criação de endpoints REST e uso dos métodos HTTP.
- Respostas HTTP como `200`, `201`, `204` e `404`.
- Injeção de dependências por construtor.
- Persistência com JPA e consultas a dados relacionados.
- Transações com Spring.
- Migrations com Flyway para manter o schema do banco em sincronia com o código.
- Configuração por variáveis de ambiente, sem deixar credenciais no código.
- Uso de records como DTOs de entrada e saída.

## Organização do código

```text
src/main/java/br/com/joaojps/jobtrack_api/
├── controller/          # Recebe as requisições HTTP
├── database/
│   ├── model/            # Entidades e status dos projetos
│   └── repository/       # Acesso ao banco de dados
├── dto/
│   ├── request/          # Dados recebidos pela API
│   └── response/         # Dados devolvidos pela API
└── service/              # Operações e regras da aplicação

src/main/resources/
├── db/migration/         # Migrations do Flyway
└── application.yaml      # Configurações da aplicação
```

## Rotas disponíveis

A URL base local é `http://localhost:8080`.

### Projetos

| Método | Rota | Ação |
| --- | --- | --- |
| `GET` | `/api/projects` | Lista os projetos |
| `GET` | `/api/projects/{projectId}` | Busca um projeto |
| `POST` | `/api/projects` | Cria um projeto |
| `PUT` | `/api/projects/{projectId}` | Atualiza os dados do projeto |
| `PATCH` | `/api/projects/{projectId}/status` | Atualiza o status |
| `DELETE` | `/api/projects/{projectId}` | Remove o projeto |

Status aceitos: `PLANEJADO`, `EM_ANDAMENTO`, `PAUSADO`, `CONCLUIDO` e `CANCELADO`.

Exemplo de corpo para criar ou atualizar projeto:

```json
{
  "nome": "JobTrack API",
  "objetivo": "Acompanhar meus projetos de desenvolvimento",
  "status": "EM_ANDAMENTO"
}
```

Exemplo de corpo para mudar o status:

```json
{
  "status": "CONCLUIDO"
}
```

### Stacks de um projeto

| Método | Rota | Ação |
| --- | --- | --- |
| `GET` | `/api/projects/{projectId}/stacks` | Lista as stacks do projeto |
| `POST` | `/api/projects/{projectId}/stacks` | Adiciona uma stack |
| `PUT` | `/api/projects/{projectId}/stacks/{stackId}` | Atualiza uma stack |
| `DELETE` | `/api/projects/{projectId}/stacks/{stackId}` | Remove uma stack |

Exemplo de corpo:

```json
{
  "nome": "Spring Boot",
  "versao": "4.1.1"
}
```

### Matérias de um projeto

| Método | Rota | Ação |
| --- | --- | --- |
| `GET` | `/api/projects/{projectId}/subjects` | Lista as matérias do projeto |
| `POST` | `/api/projects/{projectId}/subjects` | Adiciona uma matéria |
| `PUT` | `/api/projects/{projectId}/subjects/{subjectId}` | Atualiza uma matéria |
| `DELETE` | `/api/projects/{projectId}/subjects/{subjectId}` | Remove uma matéria |

Exemplo de corpo:

```json
{
  "nome": "Persistência com JPA",
  "descricao": "Mapeamento de entidades e relacionamentos com o banco"
}
```

## Como executar localmente

### Pré-requisitos

- Java 17.
- Docker Desktop com Docker Compose.

### 1. Configure o banco

Crie um arquivo `.env` na raiz do projeto com os dados locais do PostgreSQL. Por exemplo:

```dotenv
DB_NAME=jobtrackapi
DB_URL=jdbc:postgresql://localhost:5432/jobtrackapi
DB_USERNAME=jobtrack
DB_PASSWORD=troque-por-uma-senha-local
```

Use uma senha local e não envie o arquivo `.env` ao GitHub. O `.env` já está no `.gitignore`.

### 2. Inicie o PostgreSQL

```bash
docker compose up -d
```

### 3. Inicie a API

No macOS ou Linux:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Na primeira inicialização, o Flyway aplica as migrations e o JPA confere se as tabelas batem com os modelos.

## Documentação da API

O Springdoc OpenAPI já está incluído no projeto. Com a aplicação rodando, a interface fica disponível em:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Especificação OpenAPI: `http://localhost:8080/v3/api-docs`

A documentação detalhada dos campos, exemplos e respostas de erro pode ser completada junto com cada endpoint. Assim ela acompanha as mudanças da API e, no final, fazemos uma revisão geral para garantir que está completa.
