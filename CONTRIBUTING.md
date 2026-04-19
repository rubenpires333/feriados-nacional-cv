# 🤝 Guia de Contribuição - Feriados Nacionais CV

Obrigado por querer contribuir com o projeto! Este guia vai te ajudar a adicionar novos feriados e melhorar a API.

## 📋 Tipos de Contribuição

### 1. 🗓 Adicionando Feriados em Falta

#### Informações Necessárias

Para cada feriado, precisamos de:

- **Nome oficial** (português)
- **Nome em crioulo** (se disponível)
- **Data**: fixa (dia/mês) ou móvel (baseada na Páscoa)
- **Tipo**: Nacional, Municipal ou Tradicional
- **Categoria**: Civil, Religioso, Popular/Cultural ou Religioso/Civil
- **Abrangência**: Todo país, ilha específica ou município
- **Fonte**: Decreto, lei ou documento oficial
- **Descrição**: Breve explicação sobre o feriado

#### Template para Issues

```markdown
## Novo Feriado: [Nome do Feriado]

### Informações Básicas
- **Nome**: 
- **Nome Crioulo**: 
- **Data**: DD/MM (fixa) ou [relação com Páscoa] (móvel)
- **Tipo**: Nacional/Municipal/Tradicional
- **Categoria**: Civil/Religioso/Popular_Cultural/Religioso_Civil

### Abrangência
- [ ] Nacional (todo o país)
- [ ] Ilha: [nome da ilha]
- [ ] Município: [nome do município]

### Documentação
- **Decreto/Lei**: 
- **Fonte**: 
- **Descrição**: 

### Informações Adicionais
[Qualquer contexto histórico ou cultural relevante]
```

### 2. 🔧 Melhorias no Código

#### Áreas que Precisam de Atenção

- **Validações**: Melhorar validação de dados de entrada
- **Performance**: Otimizar consultas ao banco
- **Testes**: Adicionar mais testes unitários e de integração
- **Documentação**: Melhorar comentários no código
- **Internacionalização**: Suporte a mais idiomas

#### Padrões de Código

- Use **Java 21** features quando apropriado
- Siga as convenções do **Spring Boot**
- Mantenha **cobertura de testes** acima de 80%
- Use **nomes descritivos** para variáveis e métodos
- Adicione **JavaDoc** para métodos públicos

## 🗓 Feriados Conhecidos em Falta

### Feriados Nacionais Possíveis
- [ ] Dia da Mulher Cabo-verdiana
- [ ] Dia da Criança Cabo-verdiana
- [ ] Dia da Cultura Nacional
- [ ] Outros feriados comemorativos

### Feriados Municipais por Ilha

#### Santiago
- [ ] **Praia**: Festivais específicos
- [ ] **Ribeira Grande**: Celebrações locais
- [ ] **Tarrafal**: Feriados municipais
- [ ] **São Domingos**: Santos padroeiros
- [ ] **Santa Catarina**: Celebrações religiosas
- [ ] **São Salvador do Mundo**: Feriados locais
- [ ] **Santa Cruz**: Festivais tradicionais
- [ ] **São Lourenço dos Órgãos**: Celebrações

#### São Vicente
- [ ] **Mindelo**: Festival de música, Carnaval específico
- [ ] **São Pedro**: Celebrações do santo padroeiro

#### Santo Antão
- [ ] **Ribeira Grande**: Feriados municipais
- [ ] **Porto Novo**: Celebrações locais
- [ ] **Paúl**: Santos padroeiros

#### Fogo
- [ ] **São Filipe**: Feriados municipais
- [ ] **Mosteiros**: Celebrações locais
- [ ] **Santa Catarina do Fogo**: Santos padroeiros

#### Brava
- [ ] **Nova Sintra**: Feriados municipais específicos

#### Maio
- [ ] **Porto Inglês**: Celebrações locais

#### Sal
- [ ] **Espargos**: Feriados municipais
- [ ] **Santa Maria**: Festivais de turismo

#### Boa Vista
- [ ] **Sal Rei**: Feriados municipais específicos

#### São Nicolau
- [ ] **Ribeira Brava**: Feriados municipais
- [ ] **Tarrafal de São Nicolau**: Celebrações locais

### Feriados Tradicionais/Culturais
- [ ] **Festivais de Colá San Jon**: Celebrações tradicionais
- [ ] **Festivais de Batuku**: Música tradicional
- [ ] **Festivais de Funaná**: Celebrações musicais
- [ ] **Festivais de Morna**: Música tradicional
- [ ] **Romarias**: Peregrinações religiosas
- [ ] **Festas de Bandeira**: Celebrações comunitárias

## 💻 Como Implementar Novos Feriados

### 1. Feriados Fixos (Data Específica)

Edite `src/main/java/feriados_nacionais/cv/config/DataLoader.java`:

```java
private void criarFeriadosNacionais() {
    // Adicione aqui
    Feriado novoFeriado = Feriado.builder()
        .nome("Nome do Feriado")
        .nomeKriolu("Nomi na Kriolu") // opcional
        .categoria(CategoriaFeriado.CIVIL) // CIVIL, RELIGIOSO, POPULAR_CULTURAL, RELIGIOSO_CIVIL
        .tipo(TipoFeriado.NACIONAL) // NACIONAL, MUNICIPAL, TRADICIONAL, COMEMORATIVO
        .movel(false)
        .diaFixo(15) // dia do mês (1-31)
        .mesFixo(8)  // mês (1-12)
        .descricao("Descrição do feriado")
        .decreto("Decreto nº XXX/XXXX") // opcional
        .ativo(true)
        .build();
    
    feriadoRepository.save(novoFeriado);
}
```

### 2. Feriados Móveis (Baseados na Páscoa)

```java
private void criarFeriadosMoveis() {
    Feriado feriadoMovel = Feriado.builder()
        .nome("Nome do Feriado Móvel")
        .categoria(CategoriaFeriado.RELIGIOSO)
        .tipo(TipoFeriado.NACIONAL)
        .movel(true)
        .regraMovel(RegraMovel.PASCOA_MENOS_47) // ou outra regra
        .descricao("Descrição do feriado móvel")
        .ativo(true)
        .build();
    
    feriadoRepository.save(feriadoMovel);
}
```

### 3. Feriados Municipais

```java
private void criarFeriadosMunicipais() {
    // 1. Criar o feriado
    Feriado feriadoMunicipal = Feriado.builder()
        .nome("Santo Padroeiro do Município")
        .categoria(CategoriaFeriado.RELIGIOSO)
        .tipo(TipoFeriado.MUNICIPAL)
        .movel(false)
        .diaFixo(24)
        .mesFixo(6)
        .descricao("Celebração do santo padroeiro")
        .ativo(true)
        .build();
    
    feriadoRepository.save(feriadoMunicipal);
    
    // 2. Buscar o município
    Municipio municipio = municipioRepository.findByCodigo("PRA")
        .orElseThrow(() -> new RuntimeException("Município não encontrado"));
    
    // 3. Criar abrangência municipal
    FeriadoAbrangencia abrangencia = FeriadoAbrangencia.builder()
        .feriado(feriadoMunicipal)
        .scopeTipo(ScopoTipo.MUNICIPIO)
        .scopeId(municipio.getId())
        .nota("Feriado municipal específico") // opcional
        .build();
    
    feriadoAbrangenciaRepository.save(abrangencia);
}
```

### 4. Novas Regras de Feriados Móveis

Se precisar de uma nova regra de cálculo, edite `RegraMovel.java`:

```java
public enum RegraMovel {
    // Existentes...
    PASCOA_MENOS_47("pascoa_menos_47", -47, "Carnaval (Terça-feira)"),
    
    // Nova regra
    PASCOA_MENOS_X("pascoa_menos_x", -X, "Descrição do novo feriado");
    
    // Construtor e métodos...
}
```

## 🧪 Testando Suas Mudanças

### 1. Testes Locais

```bash
# Executar todos os testes
mvn test

# Executar aplicação localmente
mvn spring-boot:run

# Verificar se o feriado foi adicionado
curl -H "Authorization: Bearer SEU_TOKEN" \
     "http://localhost:8083/api/v1/feriados/2024"
```

### 2. Verificações Importantes

- [ ] Feriado aparece na lista do ano correto
- [ ] Data está sendo calculada corretamente
- [ ] Dia da semana está em português
- [ ] Abrangência (nacional/municipal) está correta
- [ ] Não há duplicatas

### 3. Swagger UI

Acesse `http://localhost:8083/swagger-ui.html` para testar os endpoints interativamente.

## 📝 Processo de Pull Request

### 1. Preparação

```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/SEU_USUARIO/feriados-nacionais-cv.git
cd feriados-nacionais-cv

# Crie uma branch para sua contribuição
git checkout -b feature/adicionar-feriado-X
```

### 2. Implementação

- Faça as mudanças necessárias
- Teste localmente
- Commit com mensagens descritivas

```bash
git add .
git commit -m "Adiciona feriado municipal de [Nome do Município]

- Adiciona celebração do Santo Padroeiro
- Inclui data fixa em DD/MM
- Adiciona abrangência municipal
- Fonte: Decreto nº XXX/XXXX"
```

### 3. Pull Request

```bash
git push origin feature/adicionar-feriado-X
```

No GitHub:
- Abra um Pull Request
- Use o template fornecido
- Descreva as mudanças claramente
- Referencie issues relacionadas

### Template de Pull Request

```markdown
## Descrição
Breve descrição das mudanças realizadas.

## Tipo de Mudança
- [ ] Novo feriado nacional
- [ ] Novo feriado municipal
- [ ] Novo feriado tradicional
- [ ] Correção de dados existentes
- [ ] Melhoria no código
- [ ] Correção de bug

## Feriados Adicionados
- **Nome**: 
- **Data**: 
- **Tipo**: 
- **Abrangência**: 

## Checklist
- [ ] Testei localmente
- [ ] Adicionei documentação necessária
- [ ] Segui os padrões de código
- [ ] Não quebrei funcionalidades existentes

## Fonte/Referência
Link ou referência para a fonte oficial do feriado.
```

## 🔍 Revisão de Código

### Critérios de Aprovação

- **Dados corretos**: Informações verificadas e precisas
- **Código limpo**: Segue padrões estabelecidos
- **Testes passando**: Todas as verificações OK
- **Documentação**: Mudanças documentadas adequadamente
- **Performance**: Não impacta negativamente a API

### Processo de Revisão

1. **Revisão automática**: GitHub Actions executam testes
2. **Revisão manual**: Maintainers verificam dados e código
3. **Feedback**: Solicitação de mudanças se necessário
4. **Aprovação**: Merge após aprovação

## 🎯 Prioridades Atuais

### Alta Prioridade
1. **Feriados municipais em falta** - Muitos municípios ainda não têm seus santos padroeiros
2. **Validação de datas** - Verificar se todas as datas estão corretas
3. **Fontes oficiais** - Adicionar referências a decretos e leis

### Média Prioridade
1. **Feriados tradicionais** - Festivais culturais regionais
2. **Melhorias na API** - Performance e novas funcionalidades
3. **Testes** - Aumentar cobertura de testes

### Baixa Prioridade
1. **Internacionalização** - Suporte a outros idiomas
2. **Interface web** - Frontend para visualização
3. **Notificações** - Sistema de alertas para feriados

## 📚 Recursos Úteis

### Documentação Oficial
- [Boletim Oficial de Cabo Verde](https://www.bo.gov.cv/)
- [Assembleia Nacional](https://www.parlamento.cv/)
- [Câmaras Municipais](https://www.governo.cv/camaras-municipais/)

### Fontes Culturais
- Instituto Nacional de Estatística
- Ministério da Cultura
- Associações culturais locais

### Ferramentas de Desenvolvimento
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Swagger/OpenAPI](https://swagger.io/docs/)

## ❓ Dúvidas Frequentes

### Como saber se um feriado é oficial?
Procure por decretos, leis ou documentos oficiais do governo ou câmaras municipais.

### Posso adicionar festivais culturais?
Sim! Use o tipo `TRADICIONAL` e categoria `POPULAR_CULTURAL`.

### Como calcular feriados móveis?
A maioria é baseada na Páscoa. Use as regras existentes ou crie novas se necessário.

### E se não souber o decreto oficial?
Não tem problema! Adicione o feriado e indique na descrição que precisa de verificação oficial.

## 🙏 Agradecimentos

Toda contribuição é valiosa! Seja adicionando um feriado, corrigindo um bug ou melhorando a documentação, você está ajudando a preservar e compartilhar a cultura cabo-verdiana.

**Obrigado por contribuir! 🇨🇻**