# Multi-stage build para otimização
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar pom.xml primeiro para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte e compilar com profile docker
COPY src src
RUN mvn clean package -DskipTests -Pdocker

# Imagem final otimizada
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar wget para health checks
RUN apk add --no-cache wget

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

# Configurações JVM otimizadas para Docker
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+UseStringDeduplication -Dspring.profiles.active=docker"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

# Executar aplicação com profile docker
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]