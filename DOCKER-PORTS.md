# 🔌 Configuração de Portas - Docker

## 📋 Portas Utilizadas

### 🚀 **Desenvolvimento** (`docker-compose.yml`)
- **API Spring Boot**: `8083` → `http://localhost:8083`
- **PostgreSQL**: `5433` → `localhost:5433` (mapeado da porta interna 5432)
- **Swagger UI**: `8083/swagger-ui.html` → `http://localhost:8083/swagger-ui.html`

### 🏭 **Produção** (`docker-compose.prod.yml`)
- **Nginx (HTTP)**: `80` → `http://localhost`
- **Nginx (HTTPS)**: `443` → `https://localhost`
- **API Spring Boot**: `8083` (interno, via Nginx)
- **PostgreSQL**: `5433` → `localhost:5433` (mapeado da porta interna 5432)

## ⚠️ **Mudança de Porta PostgreSQL**

**Problema Original:**
```
Error: Bind for 0.0.0.0:5432 failed: port is already allocated
```

**Solução Implementada:**
- Mudança da porta externa do PostgreSQL de `5432` para `5433`
- Evita conflito com PostgreSQL local já instalado
- Porta interna do container continua sendo `5432`

## 🔧 **Configurações Atualizadas**

### `.env` e `.env.example`
```env
DB_PORT=5433  # ← Mudou de 5432 para 5433
```

### `docker-compose.yml`
```yaml
db:
  ports:
    - "5433:5432"  # ← Externa:Interna
```

### `docker-compose.prod.yml`
```yaml
db:
  ports:
    - "5433:5432"  # ← Externa:Interna
```

## 🌐 **URLs de Acesso**

### Desenvolvimento
```bash
# API
curl http://localhost:8083/api/v1/feriados/2024

# Swagger UI
open http://localhost:8083/swagger-ui.html

# Health Check
curl http://localhost:8083/actuator/health

# PostgreSQL (via cliente)
psql -h localhost -p 5433 -U postgres -d feriados_nacionais
```

### Produção
```bash
# API (via Nginx)
curl https://localhost/api/v1/feriados/2024

# Health Check
curl https://localhost/health

# PostgreSQL (via cliente)
psql -h localhost -p 5433 -U postgres -d feriados_nacionais
```

## 🐳 **Comandos Docker**

### Iniciar Desenvolvimento
```bash
docker-compose up --build
```

### Iniciar Produção
```bash
docker-compose -f docker-compose.prod.yml up --build -d
```

### Verificar Portas em Uso
```bash
# Ver containers rodando
docker-compose ps

# Ver portas mapeadas
docker port postgres-feriados-cv
docker port feriados-nacionais-cv
```

### Conectar ao PostgreSQL
```bash
# Via Docker exec
docker exec -it postgres-feriados-cv psql -U postgres -d feriados_nacionais

# Via cliente local
psql -h localhost -p 5433 -U postgres -d feriados_nacionais
```

## 🔍 **Troubleshooting**

### Verificar se Portas Estão Livres
```bash
# Linux/Mac
netstat -tulpn | grep :5433
netstat -tulpn | grep :8083

# Windows
netstat -an | findstr :5433
netstat -an | findstr :8083
```

### Parar Serviços que Usam Porta 5432
```bash
# Parar PostgreSQL local (Ubuntu/Debian)
sudo systemctl stop postgresql

# Parar PostgreSQL local (CentOS/RHEL)
sudo systemctl stop postgresql-13

# Ou usar porta diferente no .env
DB_PORT=5434  # Próxima porta disponível
```

### Logs de Conexão
```bash
# Ver logs do PostgreSQL
docker-compose logs db

# Ver logs da aplicação
docker-compose logs app

# Ver logs em tempo real
docker-compose logs -f
```

## ✅ **Verificação Final**

Após iniciar os containers, verificar:

1. **Containers rodando:**
   ```bash
   docker-compose ps
   # Deve mostrar ambos containers como "Up"
   ```

2. **Portas mapeadas:**
   ```bash
   docker port postgres-feriados-cv
   # Deve mostrar: 5432/tcp -> 0.0.0.0:5433
   
   docker port feriados-nacionais-cv
   # Deve mostrar: 8083/tcp -> 0.0.0.0:8083
   ```

3. **API funcionando:**
   ```bash
   curl http://localhost:8083/actuator/health
   # Deve retornar: {"status":"UP"}
   ```

4. **PostgreSQL conectando:**
   ```bash
   docker exec postgres-feriados-cv pg_isready -U postgres
   # Deve retornar: accepting connections
   ```

---

**✅ Configuração completa e funcional com portas otimizadas!**