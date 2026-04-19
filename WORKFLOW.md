# 🔄 Fluxo de Contribuição - Feriados Nacionais CV

Este documento explica como contribuir com o projeto seguindo as melhores práticas de desenvolvimento colaborativo.

## 🌳 Estrutura de Branches

### 📋 Branches Principais

- **`main`** 🔒 - **Branch de Produção** (Protegida)
  - Código estável e testado
  - Apenas maintainers podem fazer merge
  - Requer aprovação de Pull Request
  - Deploy automático (futuro)

- **`develop`** 🚧 - **Branch de Desenvolvimento**
  - Código em desenvolvimento
  - Base para novos Pull Requests
  - Testes automáticos executados
  - Integração contínua ativa

### 🔀 Branches de Feature

- **`feature/nome-da-funcionalidade`** - Para novas funcionalidades
- **`fix/nome-do-bug`** - Para correções de bugs
- **`docs/nome-da-documentacao`** - Para melhorias na documentação
- **`feriado/nome-do-feriado`** - Para adição de novos feriados

## 🚀 Como Contribuir

### 1. 🍴 Fork do Repositório

```bash
# No GitHub, clique em "Fork" no repositório
# Clone seu fork
git clone https://github.com/SEU_USUARIO/feriados-nacional-cv.git
cd feriados-nacional-cv
```

### 2. 🔧 Configurar Repositório Local

```bash
# Adicionar repositório original como upstream
git remote add upstream https://github.com/rubenpires333/feriados-nacional-cv.git

# Verificar remotes
git remote -v
```

### 3. 🌿 Criar Branch de Feature

```bash
# Atualizar develop local
git checkout develop
git pull upstream develop

# Criar nova branch para sua contribuição
git checkout -b feriado/santo-antonio-mindelo

# Ou para outras contribuições:
# git checkout -b feature/melhorar-api
# git checkout -b fix/corrigir-data-pascoa
# git checkout -b docs/atualizar-readme
```

### 4. 💻 Fazer as Alterações

#### Para Adicionar Feriados:

Edite `src/main/java/feriados_nacionais/cv/config/DataLoader.java`:

```java
// Exemplo: Adicionar feriado municipal
private void criarFeriadosMunicipais() {
    // Seu código aqui
    Feriado novoFeriado = Feriado.builder()
        .nome("Santo António")
        .categoria(CategoriaFeriado.RELIGIOSO)
        .tipo(TipoFeriado.MUNICIPAL)
        .diaFixo(13)
        .mesFixo(6)
        .descricao("Santo padroeiro de Mindelo")
        .ativo(true)
        .build();
    
    feriadoRepository.save(novoFeriado);
    
    // Definir abrangência municipal
    Municipio mindelo = municipioRepository.findByCodigo("MIN")
        .orElseThrow(() -> new RuntimeException("Município não encontrado"));
    
    FeriadoAbrangencia abrangencia = FeriadoAbrangencia.builder()
        .feriado(novoFeriado)
        .scopeTipo(ScopoTipo.MUNICIPIO)
        .scopeId(mindelo.getId())
        .build();
    
    feriadoAbrangenciaRepository.save(abrangencia);
}
```

### 5. 🧪 Testar Localmente

```bash
# Executar testes
mvn test

# Executar aplicação
mvn spring-boot:run

# Testar endpoint específico
curl -H "Authorization: Bearer SEU_TOKEN" \
     "http://localhost:8083/api/v1/feriados/2024?municipio=MIN"
```

### 6. 📝 Commit das Alterações

```bash
# Adicionar arquivos modificados
git add .

# Commit com mensagem descritiva
git commit -m "✨ Adiciona feriado de Santo António em Mindelo

- Adiciona celebração do santo padroeiro de Mindelo
- Data: 13 de junho (feriado municipal)
- Abrangência: Município de Mindelo (MIN)
- Categoria: Religioso

Fonte: Câmara Municipal de São Vicente"
```

### 7. 📤 Push e Pull Request

```bash
# Push para seu fork
git push origin feriado/santo-antonio-mindelo
```

No GitHub:
1. Vá para seu fork
2. Clique em "Compare & pull request"
3. **Base branch**: `develop` (não main!)
4. Preencha o template de PR
5. Aguarde revisão

## 🔍 Processo de Revisão

### ✅ Critérios de Aprovação

1. **Testes passando** - CI/CD deve estar verde
2. **Dados corretos** - Informações verificadas
3. **Fonte oficial** - Decreto ou documento oficial mencionado
4. **Código limpo** - Segue padrões do projeto
5. **Sem conflitos** - Branch atualizada com develop

### 🔄 Fluxo de Revisão

1. **Automático**: GitHub Actions executa testes
2. **Manual**: Maintainer revisa código e dados
3. **Feedback**: Solicitação de mudanças se necessário
4. **Aprovação**: Merge para develop
5. **Release**: Periodicamente, develop → main

## 🛡️ Proteções da Branch Main

A branch `main` tem as seguintes proteções:

- ❌ **Push direto bloqueado**
- ✅ **Requer Pull Request**
- ✅ **Requer aprovação de maintainer**
- ✅ **Requer testes passando**
- ✅ **Requer branch atualizada**

## 📋 Templates e Automação

### 🤖 GitHub Actions

- **CI/CD**: Testes automáticos em PRs
- **Segurança**: Verificação de credenciais no código
- **Build**: Compilação automática
- **Validação**: Verificação de arquivos modificados

### 📝 Templates Disponíveis

- **Issue**: Novo feriado
- **Issue**: Correção de dados
- **Pull Request**: Template padronizado

## 🎯 Boas Práticas

### ✅ Fazer

- Sempre criar branch a partir de `develop`
- Usar nomes descritivos para branches
- Fazer commits pequenos e focados
- Testar localmente antes do PR
- Incluir fonte oficial dos feriados
- Seguir o template de PR

### ❌ Evitar

- Push direto para `main` ou `develop`
- Commits muito grandes
- Falta de testes
- Dados sem fonte oficial
- Branches desatualizadas

## 🆘 Resolução de Conflitos

### Atualizar Branch com Develop

```bash
# Ir para sua branch
git checkout sua-branch

# Buscar atualizações
git fetch upstream

# Fazer rebase com develop
git rebase upstream/develop

# Resolver conflitos se houver
# Editar arquivos conflitantes
git add .
git rebase --continue

# Push forçado (cuidado!)
git push --force-with-lease origin sua-branch
```

## 📞 Suporte

### 🤔 Dúvidas?

- **Issues**: Para dúvidas sobre feriados específicos
- **Discussions**: Para conversas gerais
- **Email**: Para questões privadas

### 🐛 Problemas?

1. Verifique se seguiu o fluxo corretamente
2. Consulte a documentação (README.md)
3. Procure issues similares
4. Abra nova issue com detalhes

## 📊 Status das Branches

### 🔍 Como Verificar

```bash
# Ver todas as branches
git branch -a

# Ver status da branch atual
git status

# Ver diferenças com develop
git diff develop
```

### 📈 Métricas

- **Main**: Sempre estável
- **Develop**: Integração contínua
- **Features**: Em desenvolvimento

---

**Obrigado por contribuir! 🇨🇻**

*Juntos estamos preservando e compartilhando a cultura cabo-verdiana!*