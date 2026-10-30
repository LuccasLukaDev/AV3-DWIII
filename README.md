# Automanager - Atualização de Base (ATVIII)

Projeto Spring Boot com microserviço para gerenciamento de loja de manutenção veicular e venda de autopeças.

Implementa o diagrama de classes solicitado com **CRUD completo** e **HATEOAS** para as entidades principais.

## Funcionalidades implementadas

- Cadastro de **Empresa** (unidade comercial)
- Associação de **Usuários** à empresa
- Tipos de usuário: `CLIENTE`, `FUNCIONARIO`, `FORNECEDOR`
- Cadastro de **Veículos**, **Mercadorias** e **Serviços**
- Cadastro de **Vendas** (com mercadorias, serviços e veículo)
- **Credenciais** de acesso (usuário/senha e código de barras)
- Documentos, telefones, e-mails e endereços
- Links HATEOAS em todas as respostas de listagem e busca individual

## Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+** (ou use o wrapper `mvnw` incluso)
- (Opcional) MySQL 8 se quiser usar banco externo

## Como rodar (passo a passo)

### 1. Abra o projeto

```bash
cd automanager
```

### 2. Compile o projeto

```bash
# Linux / macOS
./mvnw clean compile

# Windows
mvnw.cmd clean compile
```

### 3. Execute a aplicação

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe na porta **8080**.

> O banco usado por padrão é o **H2 em memória**. Os dados de exemplo (empresa, usuários, veículos, vendas etc.) são carregados automaticamente no startup via `CommandLineRunner`.

### 4. Acesse o console do H2 (opcional)

Abra no navegador:

```
http://localhost:8080/h2-console
```

- **JDBC URL**: `jdbc:h2:mem:automanager`
- **User**: `sa`
- **Password**: (deixe em branco)

### 5. Teste os endpoints (com Postman, Insomnia, curl ou browser)

#### Listar todas as empresas
```
GET http://localhost:8080/empresa/empresas
```

#### Buscar empresa por ID
```
GET http://localhost:8080/empresa/empresa/1
```

#### Listar usuários
```
GET http://localhost:8080/usuario/usuarios
```

#### Listar veículos
```
GET http://localhost:8080/veiculo/veiculos
```

#### Listar mercadorias
```
GET http://localhost:8080/mercadoria/mercadorias
```

#### Listar serviços
```
GET http://localhost:8080/servico/servicos
```

#### Listar vendas
```
GET http://localhost:8080/venda/vendas
```

### Exemplos de cadastro (POST)

**Cadastrar Empresa**
```
POST http://localhost:8080/empresa/cadastro
Content-Type: application/json

{
  "razaoSocial": "Nova Oficina LTDA",
  "nomeFantasia": "Oficina Express",
  "cadastro": "2026-10-30T10:00:00",
  "endereco": {
    "estado": "SP",
    "cidade": "São José dos Campos",
    "bairro": "Centro",
    "rua": "Rua Exemplo",
    "numero": "100",
    "codigoPostal": "12200-000"
  },
  "telefones": [
    {
      "ddd": "12",
      "numero": "999998888"
    }
  ]
}
```

**Cadastrar Usuário**
```
POST http://localhost:8080/usuario/cadastro
Content-Type: application/json

{
  "nome": "João da Silva",
  "nomeSocial": "João",
  "perfis": ["CLIENTE"],
  "emails": [
    {
      "endereco": "joao@email.com"
    }
  ],
  "telefones": [
    {
      "ddd": "12",
      "numero": "988776655"
    }
  ]
}
```

**Cadastrar Veículo**
```
POST http://localhost:8080/veiculo/cadastro
Content-Type: application/json

{
  "tipo": "SUV",
  "modelo": "Corolla Cross",
  "placa": "ABC1D23"
}
```

**Cadastrar Mercadoria**
```
POST http://localhost:8080/mercadoria/cadastro
Content-Type: application/json

{
  "nome": "Filtro de óleo",
  "descricao": "Filtro original",
  "quantidade": 50,
  "valor": 45.90,
  "cadastro": "2026-10-30T10:00:00",
  "fabricao": "2026-01-01T00:00:00",
  "validade": "2028-01-01T00:00:00"
}
```

**Cadastrar Serviço**
```
POST http://localhost:8080/servico/cadastro
Content-Type: application/json

{
  "nome": "Troca de óleo",
  "descricao": "Troca de óleo + filtro",
  "valor": 120.00
}
```

### Atualizar (PUT)

```
PUT http://localhost:8080/empresa/atualizar
Content-Type: application/json

{
  "id": 1,
  "razaoSocial": "Nome atualizado LTDA",
  "nomeFantasia": "Nome fantasia atualizado"
}
```

(O mesmo padrão se aplica para `/usuario/atualizar`, `/veiculo/atualizar`, etc.)

### Excluir (DELETE)

```
DELETE http://localhost:8080/empresa/excluir/1
DELETE http://localhost:8080/usuario/excluir/1
DELETE http://localhost:8080/veiculo/excluir/1
...
```

## Estrutura do projeto

```
src/main/java/com/autobots/automanager/
├── AutomanagerApplication.java      # Main + carga de dados de exemplo
├── controles/                       # Controllers REST (CRUD + HATEOAS)
│   ├── EmpresaControle.java
│   ├── UsuarioControle.java
│   ├── VeiculoControle.java
│   ├── MercadoriaControle.java
│   ├── ServicoControle.java
│   └── VendaControle.java
├── entitades/                       # Entidades JPA (diagrama de classes)
├── enumeracoes/                     # Enums (PerfilUsuario, TipoDocumento, TipoVeiculo)
├── modelos/                         # Adicionadores de links HATEOAS
└── repositorios/                    # Spring Data JPA Repositories
```

## Usando MySQL (opcional)

1. Crie o banco:
```sql
CREATE DATABASE base;
```

2. Edite `src/main/resources/application.properties` e descomente as linhas do MySQL (comente as do H2).

3. Reinicie a aplicação.

## Observações

- Os dados de exemplo são inseridos automaticamente no startup.
- Os links HATEOAS aparecem no JSON de resposta (campo `_links`).
- O projeto usa Spring Boot **2.6.7** e Java **17**.
- Pacote das entidades permanece `entitades` (como no código original da equipe).

---

Professor: Dr. Eng. Gerson Penha  
Atividade: ATVIII - Atualização de base
