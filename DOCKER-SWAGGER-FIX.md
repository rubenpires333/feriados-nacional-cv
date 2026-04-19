# 🔧 Correção Swagger no Docker

## 🔍 **Problema Identificado:**
- Swagger funciona com `mvn spring-boot:run` (local)
- Swagger falha no Docker com "Failed to load remote configuration"
- Diferença de comportamento entre ambiente local e Docker

## 🛠️ **Correções Implementadas:**

### 1. **Profile Docker Específico**
```xml
<!-- pom.xml -->
<profiles>
    <profile>
        <id>docker</id>
        <properties>
            <excludeDevtools>true</excludeDevtools>
            <excludeDockerCompose>true</excludeDockerCompose>
        </properties>
    </profile>
</profiles>
```

### 2. **Configuração Docker Isolada**
```properties
# application-docker.properties
spring.profiles.active=docker

# DESABILITAR COMPLETAMENTE Spring Cloud
spring.cloud.config.enabled=false
spring.cloud.config.import-check.enabled=false
spring.cloud.discovery.enabled=false
spring.cloud.service-registry.auto-registration.enabled=false
spring.cloud.bootstrap.enabled=false
spring.cloud.refresh.enabled=false
spring.docker.compose.enabled=false

# Logs otimizados para Docker
logging.level.org.springframework.cloud=ERROR
logging.level.org.hibernate.session.metrics=ERROR
```

### 3. **Dockerfile Otimizado**
```dockerfile
# Build com profile docker
RUN mvn clean package -DskipTests -Pdocker

# JVM com profile docker
ENV JAVA_OPTS="-Dspring.profiles.active=docker ..."

# Instalar wget para health checks
RUN apk add --no-cache wget
```

### 4. **Docker Compose Simplificado**
```yaml
environment:
  - SPRING_PROFILES_ACTIVE=docker
  # Removidas variáveis de log desnecessárias
  # Configurações vêm do application-docker.properties
```

## 🧪 **Como Testar:**

### 1. **Rebuild Completo**
```bash
# Parar containers existentes
docker-compose down

# Rebuild com novas configurações
docker-compose up --build

# Aguardar mensagem: "🚀 API pronta para uso!"
```

### 2. **Teste Automatizado**
```bash
chmod +x test-docker-swagger.sh
./test-docker-swagger.sh
```

### 3. **Teste Manual**
```bash
# 1. Verificar health
curl http://localhost:8083/actuator/health

# 2. Verificar API docs
curl http://localhost:8083/v3/api-docs

# 3. Abrir Swagger no navegador
open http://localhost:8083/swagger-ui.html
```

## 🎯 **Diferenças Local vs Docker:**

### **Local (`mvn spring-boot:run`)**
- Profile: `default`
- Spring Cloud: Pode estar presente mas não ativo
- DevTools: Ativo
- Docker Compose: Desabilitado

### **Docker (novo)**
- Profile: `docker`
- Spring Cloud: **Completamente desabilitado**
- DevTools: **Desabilitado**
- Docker Compose: **Desabilitado**
- Logs: **Otimizados**

## 🔍 **Diagnóstico:**

### ✅ **Sinais de Sucesso:**
```
🚀 API pronta para uso!
Health: {"status":"UP"}
API Docs: {"openapi":"3.0.1",...}
Swagger UI: Carrega sem erros
```

### ❌ **Sinais de Problema:**
```
Failed to load remote configuration
ERROR: Could not resolve placeholder
Swagger UI: Página em branco
```

## 📋 **Checklist de Verificação:**

- [ ] **Containers rodando**: `docker-compose ps`
- [ ] **Profile ativo**: Logs mostram `spring.profiles.active=docker`
- [ ] **Health OK**: `curl http://localhost:8083/actuator/health`
- [ ] **API Docs OK**: `curl http://localhost:8083/v3/api-docs`
- [ ] **Swagger UI OK**: `http://localhost:8083/swagger-ui.html`
- [ ] **Sem erros de configuração**: Logs limpos
- [ ] **API funcionando**: Endpoints retornam dados

## 🚀 **Próximos Passos:**

1. **Execute o rebuild**: `docker-compose down && docker-compose up --build`
2. **Aguarde inicialização**: Procure por "🚀 API pronta para uso!"
3. **Execute teste**: `./test-docker-swagger.sh`
4. **Abra Swagger**: http://localhost:8083/swagger-ui.html
5. **Use token**: Cole o token JWT no botão "Authorize"

## 🔧 **Se Ainda Houver Problemas:**

### 1. **Verificar Profile Ativo**
```bash
docker-compose logs app | grep "spring.profiles.active"
# Deve mostrar: spring.profiles.active=docker
```

### 2. **Verificar Logs de Erro**
```bash
docker-compose logs app | grep -i "error\|exception\|failed"
```

### 3. **Reset Completo**
```bash
docker-compose down -v
docker system prune -f
docker-compose up --build
```

---

**🎉 Com essas correções, o Swagger deve funcionar perfeitamente no Docker!**

A separação de profiles garante que o ambiente Docker tenha configurações otimizadas e isoladas, evitando conflitos com Spring Cloud e outras dependências problemáticas.