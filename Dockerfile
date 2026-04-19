# Dockerfile simples e otimizado
FROM maven:3.9.6-eclipse-temurin-21-alpine

WORKDIR /app

# Copiar pom.xml primeiro para cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Expor porta
EXPOSE 8080

# Configurações JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Executar em modo desenvolvimento com hot reload
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=true"]