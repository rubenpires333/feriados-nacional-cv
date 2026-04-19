#!/bin/bash

echo "🔧 Testando Swagger no Docker..."

# Aguardar a aplicação iniciar
echo "⏳ Aguardando aplicação iniciar..."
sleep 30

# Testar se a aplicação está rodando
echo "🔍 Testando se a aplicação está rodando..."
curl -f http://localhost:8083/actuator/health || {
    echo "❌ Aplicação não está respondendo"
    exit 1
}

# Testar API Docs
echo "📋 Testando API Docs..."
curl -f http://localhost:8083/v3/api-docs || {
    echo "❌ API Docs não está acessível"
    exit 1
}

# Testar Swagger UI
echo "🎨 Testando Swagger UI..."
curl -f http://localhost:8083/swagger-ui.html || {
    echo "❌ Swagger UI não está acessível"
    exit 1
}

echo "✅ Swagger está funcionando corretamente!"
echo "🌐 Acesse: http://localhost:8083/swagger-ui.html"