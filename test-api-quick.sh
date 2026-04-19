#!/bin/bash

# Script rápido para testar se a API está funcionando
echo "🧪 Teste rápido da API..."

# Token da configuração
TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjU5NTI2NTdhMzg1YzQ1YjJkODBmMDAxYzU1YjNjNzU5In0.eyJpc3MiOiJodHRwczovL2ZlcmlhZG9zLW5hY2lvbmFsLmtvbmVrdGFkZXYuY3YiLCJhdWQiOiJrb25la3RhRGV2Iiwic3ViIjoiNWJlODYzNTkwNzNjNDM0YmFkMmRhMzkzMjIyMmRhYmUiLCJleHAiOjE3NzY2MDg1MTksImlhdCI6MTc3NjYwODIxOX0.DNMqOA4YtPdy-I08Bl9wZ-R1qaseYtjAksXgbl8DD07ZKmJafy7R26TkaSYN0QxF5TB3Riv3KZIr_djtrpvGLg"

echo "🏥 1. Testando Health Check..."
health=$(curl -s http://localhost:8083/actuator/health)
echo "Resposta: $health"

if echo "$health" | grep -q '"status":"UP"'; then
    echo "✅ Health Check - OK"
else
    echo "❌ Health Check - PROBLEMA"
    exit 1
fi

echo ""
echo "🏝️ 2. Testando Lista de Ilhas..."
ilhas=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8083/api/v1/ilhas)
echo "Resposta: $ilhas"

if echo "$ilhas" | grep -q "Santiago\|São Vicente"; then
    echo "✅ Lista de Ilhas - OK"
else
    echo "❌ Lista de Ilhas - PROBLEMA"
fi

echo ""
echo "🏛️ 3. Testando Lista de Municípios..."
municipios=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8083/api/v1/municipios)
echo "Resposta: $municipios"

if echo "$municipios" | grep -q "Praia\|Mindelo"; then
    echo "✅ Lista de Municípios - OK"
else
    echo "❌ Lista de Municípios - PROBLEMA"
fi

echo ""
echo "🗓️ 4. Testando Feriados 2024..."
feriados=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8083/api/v1/feriados/2024)
echo "Resposta (primeiros 200 chars): ${feriados:0:200}..."

if echo "$feriados" | grep -q "Ano Novo\|Independência"; then
    echo "✅ Feriados 2024 - OK"
else
    echo "❌ Feriados 2024 - PROBLEMA"
fi

echo ""
echo "🎉 Teste concluído!"
echo "📊 Se todos os testes passaram, a API está funcionando corretamente!"
echo ""
echo "🌐 URLs para testar manualmente:"
echo "   - Swagger: http://localhost:8083/swagger-ui.html"
echo "   - Health: http://localhost:8083/actuator/health"