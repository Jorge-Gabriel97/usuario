# API de Gestão de Usuários (`usuario`)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data-JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

## 📋 Visão Geral

API RESTful desenvolvida em **Java com Spring Boot** para cadastro, autenticação e gerenciamento de usuários, endereços e telefones. A aplicação implementa arquitetura em camadas com isolamento entre entidades e DTOs, criptografia de senhas com **BCrypt** e controle de acesso stateless baseado em tokens **JWT (JSON Web Token)**.

---

## ✨ Funcionalidades

* **Cadastro de Usuários:** Registro completo com validação de unicidade de e-mail (`ConflictException`) e hashing de senha via `BCryptPasswordEncoder`.
* **Autenticação Stateless (JWT):** Login via `AuthenticationManager` com emissão de token assinado em `HS256` e validade de 1 hora.
* **Consulta e Atualização de Perfil:** Busca por e-mail e atualização parcial/total dos dados cadastrais do usuário autenticado.
* **Exclusão Segura de Conta:** Remoção de registro vinculada à verificação de titularidade (compara o *subject* do token JWT com o e-mail alvo, bloqueando exclusões de terceiros com `AccessDeniedException`).
* **Gestão de Endereços e Telefones:** Cadastro e atualização individual de múltiplos endereços e telefones vinculados ao ID do usuário extraído do token.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem & Core:** Java 17+, Spring Boot 3
* **Segurança & Autenticação:** Spring Security, JJWT (`io.jsonwebtoken` 0.12.x), BCrypt
* **Persistência:** Spring Data JPA / Hibernate, Banco de Dados Relacional (PostgreSQL / MySQL)
* **Produtividade & Build:** Lombok, Maven
* **Versionamento:** Git & GitHub (padrão *Conventional Commits*)

---

## 🏗️ Arquitetura e Fluxo de Dados

O projeto segue uma estrutura modular orientada à separação de responsabilidades e rastreabilidade ponta a ponta (*Follow the Data Flow*):

```text
src/main/java/com/jorge/usuario
├── business/
│   ├── converter/      # Mapeamento entre Entidades JPA e DTOs (UsuarioConverter)
│   ├── dto/            # Objetos de transferência de dados (UsuarioDTO, EnderecoDTO, TelefoneDTO)
│   └── UsuarioService  # Regras de negócio, transações e validação de titularidade via JWT
├── controller/
│   └── UsuarioController # Exposição dos endpoints REST (/usuario)
└── infrastructure/
    ├── entity/         # Entidades mapeadas (Usuario, Enderecos, Telefone)
    ├── exceptions/     # Exceções customizadas (ConflictException, ResourceNotFoundException)
    ├── repository/     # Interfaces Spring Data JPA
    └── security/       # Configuração de filtros, SecurityConfig e utilitário JwtUtil
```

### Mapeamento de Endpoints (`/usuario`)

| Método | Rota | Descrição | Autenticação |
| :--- | :--- | :--- | :--- |
| `POST` | `/usuario` | Cadastra um novo usuário com endereços e telefones | Pública |
| `POST` | `/usuario/login` | Autentica credenciais e retorna o token JWT | Pública |
| `GET` | `/usuario?email={email}` | Busca os dados de um usuário pelo e-mail | Bearer Token |
| `PUT` | `/usuario` | Atualiza dados cadastrais do usuário logado | Bearer Token |
| `DELETE` | `/usuario/{email}` | Exclui a conta do próprio usuário autenticado | Bearer Token |
| `POST` | `/usuario/endereco` | Adiciona um novo endereço ao usuário logado | Bearer Token |
| `PUT` | `/usuario/endereco?id={id}` | Atualiza um endereço existente pelo ID | Bearer Token |
| `POST` | `/usuario/telefone` | Adiciona um novo telefone ao usuário logado | Bearer Token |
| `PUT` | `/usuario/telefone?id={id}` | Atualiza um telefone existente pelo ID | Bearer Token |

---

## 🚀 Instalação, Configuração e Deploy Local

### Pré-requisitos

* **JDK 17** ou superior instalado
* **Maven 3.8+**
* Instância de banco de dados relacional (PostgreSQL ou MySQL) ativa

### Passo a passo

1. **Clone o repositório:**

   ```bash
   git clone [https://github.com/Jorge-Gabriel97/usuario.git](https://github.com/Jorge-Gabriel97/usuario.git)
   cd usuario
   ```

2. **Configure o arquivo `src/main/resources/application.properties`:**
   Defina as credenciais do banco de dados e uma chave secreta JWT com **no mínimo 32 caracteres** (requisito de 256 bits do algoritmo `HS256`):

   ```properties
   spring.application.name=usuario

   # Configuração do Banco de Dados
   spring.datasource.url=jdbc:postgresql://localhost:5432/usuario_db
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   # Chave secreta JWT (mínimo 32 caracteres)
   jwt.secret=sua-chave-secreta-super-segura-com-32-caracteres-ou-mais
   ```

3. **Compile e execute a aplicação:**

   ```bash
   ./mvnw spring-boot:run
   ```

   A API estará disponível em `http://localhost:8080`.

---

## 🔧 Resolução de Problemas e Desafios Técnicos

* **Sanitização de Tokens e Compatibilidade JJWT 0.12.x (`MalformedJwtException` / `WeakKeyException`):**
  * *Problema:* O corte fixo com `substring(7)` corrompia tokens enviados sem o prefixo `"Bearer "`, enquanto chaves `jwt.secret` curtas falhavam na assinatura `HS256`.
  * *Solução:* Implementado método de limpeza defensiva (`extrairTokenLimpo` / `limparToken`) antes do *parse* das claims, atualizado o `JwtUtil` para a API moderna do JJWT (`verifyWith` e `parseSignedClaims`) e padronizada a chave com 256+ bits.
* **Integridade Transacional e Chaves Estrangeiras no JPA (`TransactionRequiredException`):**
  * *Problema:* Operações de remoção derivada (`deleteByEmail`) exigiam contexto transacional explícito, e atributos primitivos (`long`) nas chaves estrangeiras podiam assumir valor `0` antes da persistência.
  * *Solução:* Adicionada anotação `@Transactional` nos métodos de escrita/exclusão do `UsuarioService` e padronizados os identificadores relacionais (`usuario_id`) como tipos *wrapper* (`Long`) nas entidades `Enderecos` e `Telefone`.
* **Desacoplamento de Camadas:**
  * *Solução:* Centralização das conversões e atualizações parciais (`null-safe`) na classe `UsuarioConverter`, impedindo a exposição direta de entidades JPA e hashes de senha nas respostas da API.

---

## 📬 Contato

**Jorge Gabriel Gomes Amparo** — Desenvolvedor Full Stack

* **GitHub:** [github.com/Jorge-Gabriel97](https://github.com/Jorge-Gabriel97)
* **Portfólio:** [wwwjorgegabrielcombr.vercel.app](https://wwwjorgegabrielcombr.vercel.app)
