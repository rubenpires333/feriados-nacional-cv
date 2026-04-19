#!/bin/bash

# Script para testar se os dados foram carregados corretamente no Docker
# Uso: ./test-docker-data.sh

echo "🧪 Testando carregamento de dados no Docker..."

# Aguardar aplicação iniciar
echo "⏳ Aguardando aplicação iniciar (60 segundos)..."
sleep 60

# Função para testar endpoint
test_endpoint() {
    local endpoint=$1
    local description=$2
    
    echo "🔍 Testando: $description"
    
    response=$(curl -s -w "%{http_code}" \
        -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjU5NTI2NTdhMzg1YzQ1YjJkODBmMDAxYzU1YjNjNzU5In0.eyJpc3MiOiJodHRwczovL2ZlcmlhZG9zLW5hY2lvbmFsLmtvbmVrdGFkZXYuY3YiLCJhdWQiOiJrb25la3RhRGV2Iiwic3ViIjoiNWJlODYzNTkwNzNjNDM0YmFkMmRhMzkzMjIyMmRhYmUiLCJleHAiOjE3NzY2MDg1MTksImlhdCI6MTc3NjYwODIxOX0.DNMqOA4YtPdy-I08Bl9wZ-R1qaseYtjAksXgbl8DD07ZKmJafy7R26TkaSYN0QxF5TB3Riv3KZIr_djtrpvGLg" \
        "http://localhost:8083$endpoint")
    
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        echo "✅ $description - OK"
        # Contar itens na resposta
        count=$(echo "$body" | jq '. | length' 2>/dev/null || echo "N/A")
        echo "   📊 Itens encontrados: $count"
    else
        echo "❌ $description - ERRO (HTTP $http_code)"
        echo "   📄 Resposta: $body"
    fi
    echo ""
}

# Testar health check
echo "🏥 Testando Health Check..."
health_response=$(curl -s "http://localhost:8083/actuator/health")
health_status=$(echo "$health_response" | jq -r '.status' 2>/dev/null || echo "UNKNOWN")

if [ "$health_status" = "UP" ]; then
    echo "✅ Health Check - OK"
else
    echo "❌ Health Check - ERRO"
    echo "   📄 Resposta: $health_response"
fi
echo ""

# Testar endpoints principais
test_endpoint "/api/v1/ilhas" "Lista de Ilhas"
test_endpoint "/api/v1/municipios" "Lista de Municípios"
test_endpoint "/api/v1/feriados/2024" "Feriados de 2024"
test_endpoint "/api/v1/feriados/hoje" "Feriado de Hoje"
test_endpoint "/api/v1/feriados/proximo" "Próximo Feriado"

# Testar feriados por município
test_endpoint "/api/v1/feriados/2024?municipio=PRA" "Feriados de Praia"
test_endpoint "/api/v1/feriados/2024?ilha=STG" "Feriados de Santiago"

# Testar Swagger UI
echo "🔍 Testando Swagger UI..."
swagger_response=$(curl -s -w "%{http_code}" "http://localhost:8083/swagger-ui.html")
swagger_code="${swagger_response: -3}"

if [ "$swagger_code" = "200" ]; then
    echo "✅ Swagger UI - OK"
else
    echo "❌ Swagger UI - ERRO (HTTP $swagger_code)"
fi
echo ""

echo "🎉 Teste concluído!"
echo "🌐 URLs disponíveis:"
echo "   - API: http://localhost:8083"
echo "   - Swagger: http://localhost:8083/swagger-ui.html"
echo "   - Health: http://localhost:8083/actuator/health"