#!/bin/bash

echo "🔍 Testando Swagger UI..."

# Testar se a aplicação está rodando
echo "1. Verificando se a aplicação está rodando..."
health=$(curl -s http://localhost:8083/actuator/health)
if echo "$health" | grep -q '"status":"UP"'; then
    echo "✅ Aplicação rodando"
else
    echo "❌ Aplicação não está rodando"
    exit 1
fi

# Testar API docs JSON
echo ""
echo "2. Testando API docs JSON..."
api_docs=$(curl -s http://localhost:8083/v3/api-docs)
if echo "$api_docs" | grep -q '"openapi"'; then
    echo "✅ API docs JSON funcionando"
    echo "   Título: $(echo "$api_docs" | jq -r '.info.title' 2>/dev/null || echo 'N/A')"
else
    echo "❌ API docs JSON com problema"
    echo "   Resposta: ${api_docs:0:200}..."
fi

# Testar Swagger UI HTML
echo ""
echo "3. Testando Swagger UI HTML..."
swagger_html=$(curl -s http://localhost:8083/swagger-ui.html)
if echo "$swagger_html" | grep -q "swagger-ui"; then
    echo "✅ Swagger UI HTML carregando"
else
    echo "❌ Swagger UI HTML com problema"
    echo "   Status: $(curl -s -w "%{http_code}" -o /dev/null http://localhost:8083/swagger-ui.html)"
fi

# Testar redirecionamento do Swagger
echo ""
echo "4. Testando redirecionamento do Swagger..."
swagger_redirect=$(curl -s -L http://localhost:8083/swagger-ui/)
if echo "$swagger_redirect" | grep -q "swagger-ui"; then
    echo "✅ Redirecionamento funcionando"
else
    echo "⚠️ Redirecionamento pode ter problema"
fi

# Testar se AuthInterceptor está permitindo Swagger
echo ""
echo "5. Testando se AuthInterceptor permite Swagger..."
swagger_status=$(curl -s -w "%{http_code}" -o /dev/null http://localhost:8083/swagger-ui.html)
if [ "$swagger_status" = "200" ]; then
    echo "✅ AuthInterceptor permitindo Swagger (status: $swagger_status)"
else
    echo "❌ AuthInterceptor bloqueando Swagger (status: $swagger_status)"
fi

echo ""
echo "🌐 URLs para testar manualmente:"
echo "   - Swagger UI: http://localhost:8083/swagger-ui.html"
echo "   - API Docs: http://localhost:8083/v3/api-docs"
echo "   - Health: http://localhost:8083/actuator/health"

echo ""
echo "🔑 Token para usar no Swagger:"
echo "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjU5NTI2NTdhMzg1YzQ1YjJkODBmMDAxYzU1YjNjNzU5In0.eyJpc3MiOiJodHRwczovL2ZlcmlhZG9zLW5hY2lvbmFsLmtvbmVrdGFkZXYuY3YiLCJhdWQiOiJrb25la3RhRGV2Iiwic3ViIjoiNWJlODYzNTkwNzNjNDM0YmFkMmRhMzkzMjIyMmRhYmUiLCJleHAiOjE3NzY2MDg1MTksImlhdCI6MTc3NjYwODIxOX0.DNMqOA4YtPdy-I08Bl9wZ-R1qaseYtjAksXgbl8DD07ZKmJafy7R26TkaSYN0QxF5TB3Riv3KZIr_djtrpvGLg"

echo ""
echo "📋 Como usar no Swagger:"
echo "1. Abra: http://localhost:8083/swagger-ui.html"
echo "2. Clique em 'Authorize' (cadeado)"
echo "3. Cole o token acima"
echo "4. Clique em 'Authorize'"
echo "5. Teste os endpoints!"