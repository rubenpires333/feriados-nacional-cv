#!/bin/bash

echo "🐳 Testando Swagger no Docker..."

# Verificar se containers estão rodando
echo "1. Verificando containers..."
if ! docker-compose ps | grep -q "Up"; then
    echo "❌ Containers não estão rodando. Execute: docker-compose up --build"
    exit 1
fi

echo "✅ Containers rodando"

# Aguardar aplicação estar pronta
echo ""
echo "2. Aguardando aplicação ficar pronta..."
for i in {1..30}; do
    if curl -s http://localhost:8083/actuator/health | grep -q '"status":"UP"'; then
        echo "✅ Aplicação pronta após ${i}0 segundos"
        break
    fi
    echo "   Tentativa $i/30..."
    sleep 10
done

# Testar health
echo ""
echo "3. Testando Health Check..."
health=$(curl -s http://localhost:8083/actuator/health)
echo "Health: $health"

# Testar API docs
echo ""
echo "4. Testando API Docs JSON..."
api_docs=$(curl -s http://localhost:8083/v3/api-docs)
if echo "$api_docs" | grep -q '"openapi"'; then
    echo "✅ API Docs JSON funcionando"
    title=$(echo "$api_docs" | jq -r '.info.title' 2>/dev/null || echo 'N/A')
    echo "   Título: $title"
else
    echo "❌ API Docs JSON com problema"
    echo "   Resposta: ${api_docs:0:300}..."
fi

# Testar Swagger UI
echo ""
echo "5. Testando Swagger UI..."
swagger_status=$(curl -s -w "%{http_code}" -o /tmp/swagger.html http://localhost:8083/swagger-ui.html)
echo "Status HTTP: $swagger_status"

if [ "$swagger_status" = "200" ]; then
    if grep -q "swagger-ui" /tmp/swagger.html; then
        echo "✅ Swagger UI carregando corretamente"
    else
        echo "⚠️ Swagger UI retorna 200 mas conteúdo pode estar incorreto"
    fi
else
    echo "❌ Swagger UI com problema (HTTP $swagger_status)"
fi

# Verificar logs da aplicação
echo ""
echo "6. Verificando logs da aplicação..."
echo "Logs recentes:"
docker-compose logs --tail=20 app | grep -E "(ERROR|WARN|Failed|Exception|🚀)"

# Testar endpoint da API
echo ""
echo "7. Testando endpoint da API..."
TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjU5NTI2NTdhMzg1YzQ1YjJkODBmMDAxYzU1YjNjNzU5In0.eyJpc3MiOiJodHRwczovL2ZlcmlhZG9zLW5hY2lvbmFsLmtvbmVrdGFkZXYuY3YiLCJhdWQiOiJrb25la3RhRGV2Iiwic3ViIjoiNWJlODYzNTkwNzNjNDM0YmFkMmRhMzkzMjIyMmRhYmUiLCJleHAiOjE3NzY2MDg1MTksImlhdCI6MTc3NjYwODIxOX0.DNMqOA4YtPdy-I08Bl9wZ-R1qaseYtjAksXgbl8DD07ZKmJafy7R26TkaSYN0QxF5TB3Riv3KZIr_djtrpvGLg"

ilhas=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8083/api/v1/ilhas)
if echo "$ilhas" | grep -q "Santiago"; then
    echo "✅ API funcionando - dados retornados"
else
    echo "❌ API com problema"
    echo "   Resposta: ${ilhas:0:200}..."
fi

echo ""
echo "🎯 Resumo:"
echo "   - Health Check: $(echo "$health" | jq -r '.status' 2>/dev/null || echo 'N/A')"
echo "   - API Docs: $([ "$api_docs" != "" ] && echo "OK" || echo "ERRO")"
echo "   - Swagger UI: $([ "$swagger_status" = "200" ] && echo "OK" || echo "ERRO")"
echo "   - API Endpoints: $(echo "$ilhas" | grep -q "Santiago" && echo "OK" || echo "ERRO")"

echo ""
echo "🌐 URLs para testar:"
echo "   - Swagger: http://localhost:8083/swagger-ui.html"
echo "   - API Docs: http://localhost:8083/v3/api-docs"
echo "   - Health: http://localhost:8083/actuator/health"

# Limpar arquivo temporário
rm -f /tmp/swagger.html