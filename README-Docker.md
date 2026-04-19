# 🐳 Executando com Docker

Este guia mostra como executar a API de Feriados Nacionais de Cabo Verde usando Docker.

## 🚀 Início Rápido

### Método 1: Script Automático (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/rubenpires333/feriados-nacional-cv.git
cd feriados-nacional-cv

# Tornar script executável (Linux/Mac)
chmod +x docker-start.sh

# Iniciar em modo desenvolvimento
./docker-start.sh dev

# Ou iniciar em modo produção
./docker-start.sh prod
```

### Método 2: Docker Compose Manual

```bash
# Clone o repositório
git clone https://github.com/rubenpires333/feriados-nacional-cv.git
cd feriados-nacional-cv

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o arquivo .env conforme necessário

# Desenvolvimento
docker-compose up --build

# Produção
docker-compose -f docker-compose.prod.yml up --build -d
```

A API estará disponível em: http://localhost:8083

## 📋 Serviços Incluídos

### API (Porta 8083)
- Spring Boot application
- Swagger UI: http://localhost:8083/swagger-ui.html
- Health check: http://localhost:8083/actuator/health

### PostgreSQL (Porta 5432)
- Banco de dados com dados iniciais
- Usuário: postgres
- Senha: postgres (configurável no .env)

## 🔧 Comandos Úteis

### Ver logs da aplicação
```bash
docker-compose logs -f app
```

### Ver logs do banco
```bash
docker-compose logs -f db
```

### Parar os serviços
```bash
docker-compose down
```

### Rebuild da aplicação
```bash
docker-compose build app
docker-compose up -d
```

### Executar apenas o banco
```bash
docker-compose up -d db
```

## 🗃 Persistência de Dados

Os dados do PostgreSQL são persistidos no volume `postgres_data`. Para limpar completamente:

```bash
docker-compose down -v
```

## 🔍 Troubleshooting

### Porta já em uso
Se a porta 8083 já estiver em uso, altere no arquivo `docker-compose.yml`:

```yaml
services:
  app:
    ports:
      - "8084:8083"  # Muda para porta 8084
```

### Problemas de conexão com banco
Verifique se o PostgreSQL está rodando:

```bash
docker-compose ps
```

### Logs de erro
```bash
docker-compose logs app | grep ERROR
```

## 🏗 Build Personalizado

### Dockerfile de Produção
```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/cv-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build manual
```bash
# Compilar a aplicação
mvn clean package -DskipTests

# Build da imagem
docker build -t feriados-cv:latest .

# Executar
docker run -p 8083:8083 \
  -e DB_HOST=host.docker.internal \
  -e API_TOKEN=seu-token-aqui \
  feriados-cv:latest
```

## 🌐 Deploy em Produção

### Variáveis de Ambiente Importantes

```env
# Produção
JPA_DDL_AUTO=validate
JPA_SHOW_SQL=false
LOG_LEVEL_ROOT=WARN
LOG_LEVEL_APP=INFO

# Segurança
API_TOKEN=token-super-seguro-de-producao
JWT_SECRET=chave-jwt-muito-segura-e-longa

# Banco
DB_HOST=seu-servidor-postgres
DB_USERNAME=usuario-producao
DB_PASSWORD=senha-super-segura
```

### Docker Compose para Produção

```yaml
version: '3.8'

services:
  app:
    image: feriados-cv:latest
    ports:
      - "8083:8083"
    environment:
      - DB_HOST=db
      - JPA_DDL_AUTO=validate
      - JPA_SHOW_SQL=false
      - LOG_LEVEL_ROOT=WARN
    depends_on:
      - db
    restart: unless-stopped

  db:
    image: postgres:15
    environment:
      POSTGRES_DB: feriados_nacionais
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  postgres_data:
```

## 📊 Monitoramento

### Health Checks
```bash
# Verificar saúde da aplicação
curl http://localhost:8083/actuator/health

# Métricas
curl http://localhost:8083/actuator/metrics
```

### Logs Estruturados
```bash
# Seguir logs em tempo real
docker-compose logs -f --tail=100 app
```

## 🔒 Segurança

### Recomendações para Produção

1. **Altere senhas padrão**
2. **Use HTTPS** (configure um reverse proxy)
3. **Limite acesso ao banco** (firewall)
4. **Monitore logs** de acesso
5. **Backup regular** do banco de dados

### Exemplo com Nginx (HTTPS)

```nginx
server {
    listen 443 ssl;
    server_name api.feriados-cv.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    location / {
        proxy_pass http://localhost:8083;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🚀 Deploy Automático

### GitHub Actions Example

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    
    - name: Build and Deploy
      run: |
        docker build -t feriados-cv:latest .
        docker-compose -f docker-compose.prod.yml up -d
```

## 📞 Suporte

Se encontrar problemas com Docker:

1. Verifique os logs: `docker-compose logs`
2. Confirme as portas disponíveis: `netstat -tulpn`
3. Teste conectividade: `docker-compose exec app ping db`
4. Abra uma issue no GitHub com detalhes do erro

---

**Feito com ❤️ e 🐳 Docker**