# 🇨🇻 Feriados Nacionais de Cabo Verde - API

Uma API REST completa para consulta dos feriados nacionais, municipais e tradicionais de Cabo Verde. Este projeto é **open source** e aceita contribuições da comunidade para manter os dados sempre atualizados.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Usar](#como-usar)
- [Endpoints da API](#endpoints-da-api)
- [Como Contribuir](#como-contribuir)
- [Adicionando Novos Feriados](#adicionando-novos-feriados)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Licença](#licença)

## 🎯 Sobre o Projeto

Esta API fornece informações completas sobre os feriados de Cabo Verde, incluindo:

- **Feriados Nacionais**: Celebrados em todo o país
- **Feriados Municipais**: Específicos de cada município (santos padroeiros)
- **Feriados Tradicionais**: Celebrações culturais e populares
- **Feriados Móveis**: Baseados no cálculo da Páscoa (Carnaval, Semana Santa, etc.)

### 🌟 Características Especiais

- ✅ **Datas sempre calculadas** para qualquer ano
- ✅ **Dias da semana em português** 
- ✅ **Autenticação por token** para uso da API
- ✅ **Documentação Swagger** integrada
- ✅ **Dados em português e crioulo** quando disponível
- ✅ **Suporte a feriados móveis** (baseados na Páscoa)

## 🚀 Funcionalidades

- Consulta de feriados por ano, mês, município ou ilha
- Verificação se uma data específica é feriado
- Consulta do próximo feriado
- Lista de feriados de hoje
- Contagem de dias úteis entre datas
- Informações sobre ilhas e municípios de Cabo Verde

## 🛠 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 4.0.5** - Framework web
- **PostgreSQL** - Banco de dados
- **Hibernate/JPA** - ORM
- **Swagger/OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização (opcional)

## ⚙️ Instalação e Configuração

### Pré-requisitos

- Java 21 ou superior
- PostgreSQL 12 ou superior
- Maven 3.6 ou superior

### 1. Clone o repositório

```bash
git clone https://github.com/rubenpires333/feriados-nacionais-cv.git
cd feriados-nacionais-cv
```

### 2. Configure o banco de dados

Crie um banco PostgreSQL:

```sql
CREATE DATABASE feriados_nacionais;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE feriados_nacionais TO postgres;
```

### 3. Configure as variáveis de ambiente

Copie o arquivo `.env.example` para `.env` e ajuste as configurações:

```bash
cp .env.example .env
```

Edite o arquivo `.env`:

```env
# Configurações do Banco de Dados
DB_HOST=localhost
DB_PORT=5432
DB_NAME=feriados_nacionais
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Token de Autenticação da API (gere um novo para produção)
API_TOKEN=seu-token-seguro-aqui

# Porta do servidor
SERVER_PORT=8083
```

### 4. Execute a aplicação

```bash
# Compilar e executar
mvn spring-boot:run

# Ou compilar e executar o JAR
mvn clean package
java -jar target/cv-0.0.1-SNAPSHOT.jar
```

### 5. Acesse a documentação

- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **API Docs**: http://localhost:8083/v3/api-docs

## 📖 Como Usar

### Autenticação

Todos os endpoints da API requerem autenticação por token, exceto a documentação Swagger.

#### Opção 1: Header Authorization
```bash
curl -H "Authorization: Bearer SEU_TOKEN" \
     "http://localhost:8083/api/v1/feriados/2026"
```

#### Opção 2: Query Parameter
```bash
curl "http://localhost:8083/api/v1/feriados/2026?token=SEU_TOKEN"
```

### Exemplos de Uso

#### Listar feriados de 2026
```bash
GET /api/v1/feriados/2026
```

#### Verificar feriado de hoje
```bash
GET /api/v1/feriados/hoje
```

#### Próximo feriado
```bash
GET /api/v1/feriados/proximo
```

#### Feriados de um município específico
```bash
GET /api/v1/feriados/2026?municipio=PRA
```

#### Lista de ilhas
```bash
GET /api/v1/ilhas
```

### Resposta Exemplo

```json
{
  "nome": "Dia da Independência",
  "data": "2026-07-05",
  "diaSemana": "Sexta-feira",
  "categoria": "CIVIL",
  "tipo": "NACIONAL",
  "movel": false,
  "descricao": "Celebração da independência de Cabo Verde"
}
```

## 🔗 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/feriados/{ano}` | Lista feriados por ano |
| GET | `/api/v1/feriados/hoje` | Feriado de hoje (se houver) |
| GET | `/api/v1/feriados/proximo` | Próximo feriado |
| GET | `/api/v1/feriados/verificar/{data}` | Verifica se uma data é feriado |
| GET | `/api/v1/feriados/moveis/{ano}` | Lista feriados móveis por ano |
| GET | `/api/v1/feriados/dias-uteis` | Conta dias úteis entre datas |
| GET | `/api/v1/ilhas` | Lista todas as ilhas |
| GET | `/api/v1/municipios` | Lista municípios (filtro por ilha opcional) |

**Parâmetros de consulta disponíveis:**
- `municipio` - Código do município (ex: PRA, MIN, SAL)
- `ilha` - Código da ilha (ex: STG, SAL, BOA)
- `tipo` - Tipo do feriado (NACIONAL, MUNICIPAL, TRADICIONAL)
- `categoria` - Categoria (CIVIL, RELIGIOSO, POPULAR_CULTURAL)

## 🤝 Como Contribuir

Agradecemos sua contribuição para manter esta API atualizada e completa! 

### 🔄 Fluxo de Contribuição

Este projeto usa um fluxo de desenvolvimento com duas branches principais:

- **`main`** 🔒 - Branch de produção (protegida, apenas maintainers)
- **`develop`** 🚧 - Branch de desenvolvimento (base para PRs)

**📋 Processo Resumido:**
1. Fork do repositório
2. Criar branch a partir de `develop`
3. Fazer alterações e testar
4. Abrir Pull Request para `develop`
5. Aguardar revisão e aprovação

**📚 Documentação Completa:** Veja [WORKFLOW.md](WORKFLOW.md) para instruções detalhadas.

### 🗓 Reportar Feriados em Falta

Se você conhece feriados que não estão na API, abra uma **Issue** com:

- Nome do feriado (português e crioulo se possível)
- Data (fixa ou móvel)
- Tipo (Nacional, Municipal, Tradicional)
- Categoria (Civil, Religioso, Popular/Cultural)
- Município/Ilha (se aplicável)
- Decreto ou fonte oficial (se disponível)
- Descrição breve

### 🔧 Corrigir Informações

Se encontrar dados incorretos:

- Abra uma **Issue** descrevendo o problema
- Inclua a fonte correta da informação
- Sugira a correção necessária

### 💻 Melhorar o Código

**📋 Processo:**
1. **Fork** do projeto
2. Crie uma **branch** a partir de `develop`: `git checkout -b feature/nova-funcionalidade`
3. Faça **commit** das mudanças: `git commit -m 'Adiciona nova funcionalidade'`
4. Faça **push** para a branch: `git push origin feature/nova-funcionalidade`
5. Abra um **Pull Request** para a branch `develop`

**⚠️ Importante:** Sempre abra PRs para `develop`, nunca diretamente para `main`!

## 📅 Adicionando Novos Feriados

### Feriados Fixos

Para adicionar um feriado com data fixa, edite o arquivo `DataLoader.java`:

```java
// Exemplo: Novo feriado nacional
Feriado novoFeriado = Feriado.builder()
    .nome("Nome do Feriado")
    .nomeKriolu("Nomi na Kriolu") // opcional
    .categoria(CategoriaFeriado.CIVIL) // ou RELIGIOSO, POPULAR_CULTURAL
    .tipo(TipoFeriado.NACIONAL) // ou MUNICIPAL, TRADICIONAL
    .movel(false)
    .diaFixo(15) // dia do mês
    .mesFixo(8)  // mês (1-12)
    .descricao("Descrição do feriado")
    .decreto("Decreto nº XXX/XXXX") // opcional
    .ativo(true)
    .build();

feriadoRepository.save(novoFeriado);
```

### Feriados Móveis

Para feriados baseados na Páscoa:

```java
// Exemplo: Feriado 40 dias antes da Páscoa
Feriado feriadoMovel = Feriado.builder()
    .nome("Nome do Feriado Móvel")
    .categoria(CategoriaFeriado.RELIGIOSO)
    .tipo(TipoFeriado.NACIONAL)
    .movel(true)
    .regraMovel(RegraMovel.PASCOA_MENOS_40) // defina a regra necessária
    .descricao("Descrição do feriado móvel")
    .ativo(true)
    .build();
```

### Feriados Municipais

Para feriados específicos de municípios:

```java
// 1. Criar o feriado
Feriado feriadoMunicipal = Feriado.builder()
    .nome("Santo Padroeiro")
    .categoria(CategoriaFeriado.RELIGIOSO)
    .tipo(TipoFeriado.MUNICIPAL)
    .movel(false)
    .diaFixo(24)
    .mesFixo(6)
    .ativo(true)
    .build();

feriadoRepository.save(feriadoMunicipal);

// 2. Definir abrangência municipal
FeriadoAbrangencia abrangencia = FeriadoAbrangencia.builder()
    .feriado(feriadoMunicipal)
    .scopeTipo(ScopoTipo.MUNICIPIO)
    .scopeId(municipio.getId()) // ID do município
    .build();

feriadoAbrangenciaRepository.save(abrangencia);
```

### Regras para Feriados Móveis

Se precisar de uma nova regra de cálculo, adicione ao enum `RegraMovel.java`:

```java
PASCOA_MENOS_X("pascoa_menos_x", -X, "Descrição do feriado"),
PASCOA_MAIS_X("pascoa_mais_x", X, "Descrição do feriado");
```

## 📁 Estrutura do Projeto

```
src/main/java/feriados_nacionais/cv/
├── config/          # Configurações (Auth, CORS, etc.)
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── model/
│   ├── entity/     # Entidades JPA
│   └── enums/      # Enumerações
├── repository/     # Repositórios JPA
├── service/        # Lógica de negócio
└── util/           # Utilitários

docs/               # Documentação adicional
├── cabo-verde-feriados.sql
└── feriados-cabo-verde-doc.docx
```

## 🗃 Dados Atuais

A API já inclui:

### Feriados Nacionais
- Ano Novo, Dia da Independência, Dia da Democracia
- Carnaval, Páscoa, Corpus Christi
- Dia do Trabalhador, Dia de Finados
- E outros...

### Feriados Municipais
- Santos padroeiros de cada município
- Datas específicas por localidade

### Feriados Tradicionais/Culturais
- Tabanka, Batuque
- Festivais de música tradicionais
- Celebrações culturais regionais

## 🚨 Feriados em Falta (Ajude-nos!)

Sabemos que ainda faltam alguns feriados importantes. Se você conhece algum, por favor contribua:

- Feriados municipais específicos
- Celebrações tradicionais regionais
- Feriados históricos ou comemorativos
- Datas culturais importantes

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🙏 Agradecimentos

- Comunidade cabo-verdiana pela preservação das tradições
- Contribuidores que ajudam a manter os dados atualizados
- Governo de Cabo Verde pelas informações oficiais

## 📞 Contato

- **Issues**: Use o sistema de Issues do GitHub
- **Discussões**: Use as Discussions para dúvidas gerais
- **Email**: [rubenpires333@gmail.com]

---

*Ajude-nos a manter esta API completa e atualizada!*