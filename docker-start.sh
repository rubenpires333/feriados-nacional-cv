#!/bin/bash

# Script para inicializar a aplicação com Docker
# Uso: ./docker-start.sh [dev|prod]

set -e

MODE=${1:-dev}

echo "🐳 Iniciando Feriados Nacionais CV em modo: $MODE"

# Verificar se .env existe
if [ ! -f .env ]; then
    echo "⚠️  Arquivo .env não encontrado!"
    echo "📋 Copiando .env.example para .env..."
    cp .env.example .env
    echo "✅ Arquivo .env criado. Edite as configurações se necessário."
fi

# Função para desenvolvimento
start_dev() {
    echo "🚀 Iniciando em modo DESENVOLVIMENTO..."
    echo "📋 Serviços: PostgreSQL (porta 5433) + Spring Boot"
    echo "🔗 URLs:"
    echo "   - API: http://localhost:8083"
    echo "   - Swagger: http://localhost:8083/swagger-ui.html"
    echo "   - Health: http://localhost:8083/actuator/health"
    echo "   - PostgreSQL: localhost:5433"
    echo ""
    
    # Parar containers existentes
    docker-compose down
    
    # Construir e iniciar
    docker-compose up --build
}

# Função para produção
start_prod() {
    echo "🏭 Iniciando em modo PRODUÇÃO..."
    echo "📋 Serviços: PostgreSQL (porta 5433) + Spring Boot + Nginx"
    echo "🔗 URLs:"
    echo "   - API: https://localhost (via Nginx)"
    echo "   - Health: https://localhost/health"
    echo "   - PostgreSQL: localhost:5433"
    echo ""
    
    # Verificar variáveis obrigatórias para produção
    if [ -z "$API_TOKEN" ] || [ -z "$JWT_SECRET" ]; then
        echo "❌ ERRO: Variáveis de produção não configuradas!"
        echo "📋 Configure no .env:"
        echo "   - API_TOKEN (token seguro)"
        echo "   - JWT_SECRET (chave JWT segura)"
        exit 1
    fi
    
    # Parar containers existentes
    docker-compose -f docker-compose.prod.yml down
    
    # Construir e iniciar
    docker-compose -f docker-compose.prod.yml up --build -d
    
    echo "✅ Aplicação iniciada em background"
    echo "📊 Para ver logs: docker-compose -f docker-compose.prod.yml logs -f"
}

# Função para parar
stop_all() {
    echo "🛑 Parando todos os containers..."
    docker-compose down
    docker-compose -f docker-compose.prod.yml down
    echo "✅ Containers parados"
}

# Função para limpar
clean_all() {
    echo "🧹 Limpando containers e volumes..."
    docker-compose down -v
    docker-compose -f docker-compose.prod.yml down -v
    docker system prune -f
    echo "✅ Limpeza concluída"
}

# Menu principal
case $MODE in
    "dev"|"development")
        start_dev
        ;;
    "prod"|"production")
        start_prod
        ;;
    "stop")
        stop_all
        ;;
    "clean")
        clean_all
        ;;
    *)
        echo "❌ Modo inválido: $MODE"
        echo ""
        echo "📋 Uso: $0 [dev|prod|stop|clean]"
        echo ""
        echo "🔧 Modos disponíveis:"
        echo "   dev  - Desenvolvimento (hot reload)"
        echo "   prod - Produção (otimizado + Nginx)"
        echo "   stop - Parar todos os containers"
        echo "   clean - Limpar containers e volumes"
        exit 1
        ;;
esac