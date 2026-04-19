# Multi-stage build para otimização
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar arquivos de configuração Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Resolver dependências (cache layer)
RUN mvn dependency:go-offline -B || true

# Copiar código fonte
COPY src src

# Compilar aplicação
RUN mvn clean package -DskipTests -B

# Imagem final otimizada
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copiar JAR da etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Configurar propriedades do sistema
RUN chown appuser:appgroup app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8083

# Configurações JVM otimizadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+UseStringDeduplication"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/feriados-nacionais/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -jar app.jar"]