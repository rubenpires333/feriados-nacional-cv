package feriados_nacionais.cv.config;

import feriados_nacionais.cv.model.entity.*;
import feriados_nacionais.cv.model.enums.*;
import feriados_nacionais.cv.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private SantoRepository santoRepository;
    @Autowired private IlhaRepository ilhaRepository;
    @Autowired private MunicipioRepository municipioRepository;
    @Autowired private FeriadoRepository feriadoRepository;
    @Autowired private FeriadoAbrangenciaRepository feriadoAbrangenciaRepository;
    @Autowired private OcorrenciaExtraordinariaRepository ocorrenciaExtraordinariaRepository;
    @Autowired private TestEntityRepository testEntityRepository;

    @Override
    public void run(String... args) throws Exception {
        if (feriadoRepository.count() > 0) {
            System.out.println("✓ Dados já existem na base de dados. Carregamento ignorado.");
            return;
        }
        System.out.println("🔄 Carregando dados completos de feriados de Cabo Verde...");
        loadCaboVerdeHolidaysData();
        System.out.println("✅ Dados de feriados de Cabo Verde carregados com sucesso!");
        System.out.println("📊 Resumo dos dados carregados:");
        System.out.println("   - Santos: "                  + santoRepository.count());
        System.out.println("   - Ilhas: "                   + ilhaRepository.count());
        System.out.println("   - Municípios: "              + municipioRepository.count());
        System.out.println("   - Feriados: "                + feriadoRepository.count());
        System.out.println("   - Abrangências: "            + feriadoAbrangenciaRepository.count());
        System.out.println("   - Ocorrências Extraordinárias: " + ocorrenciaExtraordinariaRepository.count());
    }

    private void loadCaboVerdeHolidaysData() {

        // ══════════════════════════════════════════════════════════════
        //  SANTOS
        // ══════════════════════════════════════════════════════════════

        Santo nossaSenhoraGraca  = santoRepository.save(createSanto("Nossa Senhora da Graça",  "Nosa Sinhora di Grasa",  8,  15, "Padroeira nacional de Cabo Verde. Festa da Assunção."));
        Santo nossaSenhoraAjuda  = santoRepository.save(createSanto("Nossa Senhora da Ajuda",  "Nosa Sinhora di Ajuda",  8,  15, "Padroeira do município dos Mosteiros (Fogo)."));
        Santo nossaSenhoraDores  = santoRepository.save(createSanto("Nossa Senhora das Dores", "Nosa Sinhora di Dor",    9,  15, "Padroeira do município do Sal."));
        Santo santaCatarina      = santoRepository.save(createSanto("Santa Catarina",          "Santa Katarina",         11, 25, "Padroeira de Santa Catarina de Santiago e Santa Catarina do Fogo."));
        Santo santaIsabel        = santoRepository.save(createSanto("Santa Isabel",            "Santa Izabel",           7,   4, "Padroeira do município da Boa Vista."));
        Santo santoAmaro         = santoRepository.save(createSanto("Santo Amaro",             "Santo Amaro",            1,  15, "Padroeiro do município de Tarrafal de Santiago."));
        Santo santoAntonio       = santoRepository.save(createSanto("Santo António",           "Santo Antoniu",          6,  13, "Padroeiro do município de Paúl (Santo Antão)."));
        Santo saoFilipeSanto     = santoRepository.save(createSanto("São Filipe Apóstolo",     "San Filipe",             5,   1, "Padroeiro do município de São Filipe (Fogo)."));
        Santo saoJoao            = santoRepository.save(createSanto("São João Baptista",       "San Jon Batista",        6,  24, "Padroeiro do município da Brava. Festejado em todo o Barlavento (Kolá San Jon)."));
        Santo saoMiguel          = santoRepository.save(createSanto("São Miguel",              "San Migel",              9,  29, "Padroeiro do município de São Miguel (Santiago)."));
        Santo saoNicolauSanto    = santoRepository.save(createSanto("São Nicolau",             "San Nikolau",            12,  6, "Padroeiro do município de Ribeira Brava (São Nicolau)."));
        Santo saoVicenteSanto    = santoRepository.save(createSanto("São Vicente",             "San Visenti",            1,  22, "Padroeiro do município de São Vicente e da ilha homónima."));
        Santo saoLourenco        = santoRepository.save(createSanto("São Lourenço",            "San Lourens",            8,  10, "Festejado em São Lourenço dos Órgãos."));
        Santo saoPedro           = santoRepository.save(createSanto("São Pedro",               "San Pedru",              6,  29, "Padroeiro dos pescadores. Festejado em várias ilhas."));

        // ══════════════════════════════════════════════════════════════
        //  ILHAS
        // ══════════════════════════════════════════════════════════════

        Ilha santoAntao     = ilhaRepository.save(createIlha("Santo Antão", "SA"));
        Ilha saoVicenteIlha = ilhaRepository.save(createIlha("São Vicente", "SV"));
        Ilha saoNicolauIlha = ilhaRepository.save(createIlha("São Nicolau", "SN"));
        Ilha sal            = ilhaRepository.save(createIlha("Sal",         "SL"));
        Ilha boaVista       = ilhaRepository.save(createIlha("Boa Vista",   "BV"));
        Ilha maio           = ilhaRepository.save(createIlha("Maio",        "MA"));
        Ilha santiago       = ilhaRepository.save(createIlha("Santiago",    "ST"));
        Ilha fogo           = ilhaRepository.save(createIlha("Fogo",        "FG"));
        Ilha brava          = ilhaRepository.save(createIlha("Brava",       "BR"));

        // ══════════════════════════════════════════════════════════════
        //  MUNICÍPIOS
        // ══════════════════════════════════════════════════════════════

        // Santo Antão — 4 municípios
        municipioRepository.save(createMunicipio("Santo Antão (ilha)",       "santo-antao-ilha",    1, 17, santoAntao,     null));
        municipioRepository.save(createMunicipio("Ribeira Grande",           "ribeira-grande-sa",   5,  7, santoAntao,     null));
        municipioRepository.save(createMunicipio("Paúl",                     "paul",                6, 13, santoAntao,     santoAntonio));
        municipioRepository.save(createMunicipio("Porto Novo",               "porto-novo",          9,  2, santoAntao,     null));

        // São Vicente — 1 município
        Municipio mSaoVicente = municipioRepository.save(createMunicipio("São Vicente", "sao-vicente", 1, 22, saoVicenteIlha, saoVicenteSanto));

        // São Nicolau — 2 municípios
        municipioRepository.save(createMunicipio("Tarrafal de São Nicolau",  "tarrafal-sao-nicolau", 8,  2, saoNicolauIlha, null));
        municipioRepository.save(createMunicipio("Ribeira Brava",            "ribeira-brava-sn",    12,  6, saoNicolauIlha, saoNicolauSanto));

        // Sal — 1 município
        municipioRepository.save(createMunicipio("Sal",      "sal",       9, 15, sal,      nossaSenhoraDores));

        // Boa Vista — 1 município
        municipioRepository.save(createMunicipio("Boa Vista", "boa-vista", 7,  4, boaVista, santaIsabel));

        // Maio — 1 município
        municipioRepository.save(createMunicipio("Maio",     "maio",       9,  8, maio,     null));

        // Santiago — 9 municípios
        municipioRepository.save(createMunicipio("Tarrafal de Santiago",     "tarrafal-santiago",    1, 15, santiago, santoAmaro));
        municipioRepository.save(createMunicipio("Ribeira Grande de Santiago","ribeira-grande-stg",  1, 31, santiago, null));
        municipioRepository.save(createMunicipio("São Domingos",             "sao-domingos",         3, 13, santiago, null));
        municipioRepository.save(createMunicipio("São Lourenço dos Órgãos",  "sao-lourenco-orgaos",  5,  9, santiago, saoLourenco));
        Municipio praia = municipioRepository.save(createMunicipio("Praia",  "praia",                5, 19, santiago, null));
        municipioRepository.save(createMunicipio("São Salvador do Mundo",    "sao-salvador-mundo",   7, 19, santiago, null));
        Municipio santaCruz = municipioRepository.save(createMunicipio("Santa Cruz", "santa-cruz",   7, 25, santiago, null));
        municipioRepository.save(createMunicipio("São Miguel",               "sao-miguel",           9, 29, santiago, saoMiguel));
        municipioRepository.save(createMunicipio("Santa Catarina de Santiago","santa-catarina-stg", 11, 25, santiago, santaCatarina));

        // Fogo — 3 municípios
        Municipio mSaoFilipe = municipioRepository.save(createMunicipio("São Filipe",           "sao-filipe",          5,  1, fogo, saoFilipeSanto));
        municipioRepository.save(createMunicipio("Mosteiros",               "mosteiros",            8, 15, fogo, nossaSenhoraAjuda));
        municipioRepository.save(createMunicipio("Santa Catarina do Fogo",  "santa-catarina-fogo", 11, 25, fogo, santaCatarina));

        // Brava — 1 município
        municipioRepository.save(createMunicipio("Brava", "brava", 6, 24, brava, saoJoao));

        // ══════════════════════════════════════════════════════════════
        //  FERIADOS NACIONAIS FIXOS
        // ══════════════════════════════════════════════════════════════

        Feriado anoNovo      = createFeriadoFixo("Ano Novo",                                        "Anu Novu",              TipoFeriado.NACIONAL,    CategoriaFeriado.CIVIL,             1,  1,  "Decreto-Lei 18/84 de 18-02-1984", "Celebração do início do novo ano civil.", null);
        Feriado democracia   = createFeriadoFixo("Dia da Democracia e Liberdade",                   "Dia di Demokrasia",     TipoFeriado.NACIONAL,    CategoriaFeriado.CIVICO,            1,  13, "Decreto-Lei 18/84 de 18-02-1984", "Primeiras eleições multipartidárias de 1991.", null);
        Feriado herois       = createFeriadoFixo("Dia da Nacionalidade e dos Heróis Nacionais",     "Dia di Nasionalidadi",  TipoFeriado.NACIONAL,    CategoriaFeriado.CIVICO,            1,  20, "Decreto-Lei 18/84 de 18-02-1984", "Homenagem a Amílcar Cabral, nascido a 12 de Setembro de 1924, morto a 20 de Janeiro de 1973.", null);
        Feriado trabalhador  = createFeriadoFixo("Dia do Trabalhador",                              "Dia di Trabadjer",      TipoFeriado.NACIONAL,    CategoriaFeriado.CIVIL,             5,  1,  "Decreto-Lei 18/84 de 18-02-1984", "Dia Internacional do Trabalhador.", null);
        Feriado crianca      = createFeriadoFixo("Dia Mundial da Criança",                          "Dia Mundial di Kriansa",TipoFeriado.NACIONAL,    CategoriaFeriado.CIVIL,             6,  1,  "Decreto-Lei 18/84 de 18-02-1984", "Dia Internacional das Crianças.", null);
        Feriado independencia= createFeriadoFixo("Dia da Independência",                            "Dia di Independénsia",  TipoFeriado.NACIONAL,    CategoriaFeriado.CIVICO,            7,  5,  "Decreto-Lei 18/84 de 18-02-1984", "Independência de Portugal proclamada em 5 de Julho de 1975.", null);
        Feriado assuncao     = createFeriadoFixo("Assunção de Nossa Senhora — Padroeira Nacional",  "Nosa Sinhora di Grasa", TipoFeriado.NACIONAL,    CategoriaFeriado.RELIGIOSO,         8,  15, "Decreto-Lei 18/84 de 18-02-1984", "Nossa Senhora da Graça, padroeira de Cabo Verde.", nossaSenhoraGraca);
        Feriado todosSantos  = createFeriadoFixo("Dia de Todos os Santos",                          "Dia di Tudu Santu",     TipoFeriado.NACIONAL,    CategoriaFeriado.RELIGIOSO,         11, 1,  "Decreto-Lei 18/84 de 18-02-1984", null, null);
        Feriado natal        = createFeriadoFixo("Natal",                                           "Natal",                 TipoFeriado.NACIONAL,    CategoriaFeriado.RELIGIOSO,         12, 25, "Decreto-Lei 18/84 de 18-02-1984", "Natividade de Jesus Cristo.", null);

        // ══════════════════════════════════════════════════════════════
        //  FERIADOS NACIONAIS MÓVEIS
        // ══════════════════════════════════════════════════════════════

        Feriado carnaval   = createFeriadoMovel("Terça-Feira de Carnaval", "Karnval",          TipoFeriado.NACIONAL, CategoriaFeriado.POPULAR_RELIGIOSO, RegraMovel.PASCOA_MENOS_47, "Decreto anual do Governo",              "Feriado decretado anualmente. Maior celebração em Mindelo (São Vicente).", null);
        Feriado cinzas     = createFeriadoMovel("Quarta-Feira de Cinzas",  "Kuarta di Sinza",  TipoFeriado.NACIONAL, CategoriaFeriado.RELIGIOSO,         RegraMovel.PASCOA_MENOS_46, "Decreto anual do Governo",              "Início da Quaresma. Decretado feriado nacional por decreto anual.", null);
        Feriado sextaSanta = createFeriadoMovel("Sexta-Feira Santa",       "Sesta Fera Santa", TipoFeriado.NACIONAL, CategoriaFeriado.RELIGIOSO,         RegraMovel.PASCOA_MENOS_2,  "Decreto-Lei 18/84 de 18-02-1984",      "Paixão e morte de Jesus Cristo.", null);

        // ══════════════════════════════════════════════════════════════
        //  FESTAS TRADICIONAIS — completas (SQL + JSON)
        // ══════════════════════════════════════════════════════════════

        // Já existentes (do SQL original)
        Feriado kolaSanJon    = createFeriadoFixo("Kolá San Jon",      "Kolá San Jon",       TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO, 6, 24, null, "Festa de São João Baptista com dança da colá (umbigada), tambores e fogueiras. Ilhas de Barlavento e Brava.", saoJoao);
        Feriado festaBandeira = createFeriadoFixo("Festa da Bandeira", "Festa di Bandeira",  TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO, 5,  1, null, "Festa religiosa e popular da ilha do Fogo com procissões e música de colá.", null);
        Feriado tabanka       = createFeriadoFixo("Tabanka",          "Tabanka",            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,  5, 15, null, "Performance teatral de raízes africanas (séc. XVIII). Dura 10 dias. Ilha de Santiago. Candidatura UNESCO.", null);

        // ── ADICIONADOS DO JSON (faltavam no SQL e no DataLoader) ─────

        // Carnaval de Mindelo — célébração específica de São Vicente
        Feriado carnavalMindelo = createFeriadoMovel(
            "Carnaval de Mindelo", "Karnval di Mindelu",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            RegraMovel.PASCOA_MENOS_47, null,
            "O maior carnaval de Cabo Verde, com desfile de escolas de samba, comparsas e grupos temáticos. Considerado o 'Carnaval das Ilhas'.",
            null);

        // Carnaval da Praia — variante de Santiago com influência africana
        Feriado carnavalPraia = createFeriadoMovel(
            "Carnaval da Praia", "Karnval di Praia",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            RegraMovel.PASCOA_MENOS_47, null,
            "Carnaval da capital com forte influência africana e grupos de mascarados. Conhecido pelas Batucadeiras.",
            null);

        // Batuque — dança ritual feminina de Santiago
        Feriado batuque = createFeriadoFixo(
            "Batuque", "Batuku",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            8, 20, null,
            "Dança ritual feminina de origem africana. Mulheres em círculo batem um pano dobrado entre os joelhos enquanto uma solista dança ao centro. Ritmo crescente controlado pela mais velha. Mensagens de liberdade e empoderamento.",
            null);

        // Festa de Santo António — padroeiro festejado em várias ilhas
        Feriado festaSantoAntonio = createFeriadoFixo(
            "Festa de Santo António", "Festa di Santo Antoniu",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO,
            6, 13, null,
            "Festa do padroeiro Santo António com missas, procissões e bailinhos. Especialmente festejado no município de Paúl (Santo Antão).",
            santoAntonio);

        // Festival Internacional de Música da Boa Vista — agosto, variável
        Feriado festivalMusicaBV = createFeriadoFixo(
            "Festival Internacional de Música da Boa Vista", "Festival di Musika Boa Vista",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            8, 15, null,
            "Festival de música que celebra géneros cabo-verdianos como morna, coladeira e funaná com artistas nacionais e internacionais.",
            null);

        // Festival Internacional de Batuku — bienal, Santiago/Praia
        Feriado festivalBatuku = createFeriadoFixo(
            "Festival Internacional de Batuku", "Festival Internasional di Batuku",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            9, 10, null,
            "Festival internacional que reúne grupos de Batuku da diáspora cabo-verdiana e de África. Marco cultural da identidade nacional.",
            null);

        // Festa de Nossa Senhora da Graça — todo o arquipélago, 15 Ago
        Feriado festaNSGraca = createFeriadoFixo(
            "Festa de Nossa Senhora da Graça (Assunção)", "Nosa Sinhora di Grasa",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO,
            8, 15, null,
            "Padroeira nacional de Cabo Verde. Celebração com procissões marinhas, missas solenes e festas populares em todo o arquipélago. Especialmente festejado nos Mosteiros (Fogo).",
            nossaSenhoraGraca);

        // Festa de São Pedro — padroeiro dos pescadores, 29 Jun
        Feriado festaSaoPedro = createFeriadoFixo(
            "Festa de São Pedro", "Festa di San Pedru",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO,
            6, 29, null,
            "Festa do padroeiro dos pescadores com procissões marítimas, bênção dos barcos e danças tradicionais.",
            saoPedro);

        // Festa de Finados / Dia dos Mortos — 1-2 Nov
        Feriado festaFinados = createFeriadoFixo(
            "Festa de Finados / Dia dos Mortos", "Dia di Finadu",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO,
            11, 1, null,
            "Visita aos cemitérios, limpeza e decoração dos túmulos com flores. Missas em memória dos defuntos. Tradição fortemente enraizada em Santiago.",
            null);

        // Festa de Santa Cruz (Santiago) — 3 Mai, Santa Cruz
        Feriado festaSantaCruz = createFeriadoFixo(
            "Festa de Santa Cruz (Santiago)", "Festa di Santa Krus",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_RELIGIOSO,
            5, 3, null,
            "Festa pagã e religiosa na cidade de Santa Cruz com batizados, procissões e arraiais populares.",
            null);

        // Música da Morna — Património UNESCO 2019
        Feriado morna = createFeriadoFixo(
            "Música da Morna", "Morna",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            11, 15, null,
            "A Morna, género musical de Cabo Verde, foi inscrita na Lista do Património Cultural Imaterial da UNESCO em 2019. Celebrada especialmente em São Vicente (Mindelo).",
            null);

        // Réveillon / Passagem de Ano — 31 Dez
        Feriado reveillon = createFeriadoFixo(
            "Réveillon / Passagem de Ano", "Reveyon",
            TipoFeriado.TRADICIONAL, CategoriaFeriado.POPULAR_CULTURAL,
            12, 31, null,
            "Celebração do Ano Novo com fogos de artifício, música e festas populares em todas as ilhas.",
            null);

        // ══════════════════════════════════════════════════════════════
        //  DATAS COMEMORATIVAS — completas (do JSON, faltavam todas)
        // ══════════════════════════════════════════════════════════════

        Feriado diaMulherCV   = createFeriadoFixo("Dia da Mulher Cabo-verdiana",  null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVIL, 2,  2,  null, "Homenagem à mulher cabo-verdiana.",                                      null);
        Feriado diaMulher     = createFeriadoFixo("Dia da Mulher",                null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVIL, 3,  8,  null, "Dia Internacional da Mulher.",                                           null);
        Feriado diaPai        = createFeriadoFixo("Dia do Pai",                   null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVIL, 3,  19, null, "Dia do Pai em Cabo Verde (São José).",                                   null);
        Feriado diaMae        = createFeriadoFixo("Dia da Mãe",                   null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVIL, 5,  1,  null, "Dia da Mãe em Cabo Verde (1 de Maio).",                                  null);
        Feriado diaAfrica     = createFeriadoFixo("Dia de África",                null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVICO,5,  25, null, "Dia de África — Cabo Verde integra a União Africana.",                   null);
        Feriado diaAmbiente   = createFeriadoFixo("Dia do Ambiente",              null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVIL, 6,  5,  null, "Dia Mundial do Ambiente.",                                               null);
        Feriado diaDiaspora   = createFeriadoFixo("Dia da Diáspora Cabo-verdiana",null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.CIVICO,9,  18, null, "Homenagem à comunidade cabo-verdiana no exterior.",                     null);
        Feriado veisperaNatal = createFeriadoFixo("Natal (Véspera)",              null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.RELIGIOSO, 12, 24, null, "Véspera de Natal com missas do Galo e celebrações em família.",         null);
        // Semana Santa — móvel, sem RegraMovel dedicada; usa PASCOA_MENOS_7 como aproximação
        Feriado semanaSanta   = createFeriadoMovel("Semana Santa",                null,   TipoFeriado.COMEMORATIVO, CategoriaFeriado.RELIGIOSO, RegraMovel.PASCOA_MENOS_7, null, "Semana de celebrações religiosas antes da Páscoa.",                     null);

        // ══════════════════════════════════════════════════════════════
        //  FERIADO_ABRANGENCIA
        // ══════════════════════════════════════════════════════════════

        // ── Feriados nacionais (todos → scope nacional) ───────────────
        for (Feriado f : Arrays.asList(
                anoNovo, democracia, herois, trabalhador, crianca,
                independencia, assuncao, todosSantos, natal,
                carnaval, cinzas, sextaSanta)) {
            feriadoAbrangenciaRepository.save(createAbrangencia(f, ScopoTipo.NACIONAL, null, null));
        }

        // ── Datas comemorativas → scope nacional ──────────────────────
        for (Feriado f : Arrays.asList(
                diaMulherCV, diaMulher, diaPai, diaMae, diaAfrica,
                diaAmbiente, diaDiaspora, veisperaNatal, semanaSanta)) {
            feriadoAbrangenciaRepository.save(createAbrangencia(f, ScopoTipo.NACIONAL, null, null));
        }

        // ── Kolá San Jon → 5 ilhas ────────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(kolaSanJon, ScopoTipo.ILHA, santoAntao.getId(),     "Kolá San Jon no Porto Novo, 23-25 Junho"));
        feriadoAbrangenciaRepository.save(createAbrangencia(kolaSanJon, ScopoTipo.ILHA, saoVicenteIlha.getId(), "Ribeira de Julião, Mindelo"));
        feriadoAbrangenciaRepository.save(createAbrangencia(kolaSanJon, ScopoTipo.ILHA, saoNicolauIlha.getId(), "Festejado em toda a ilha"));
        feriadoAbrangenciaRepository.save(createAbrangencia(kolaSanJon, ScopoTipo.ILHA, boaVista.getId(),       "Festejado em toda a ilha"));
        feriadoAbrangenciaRepository.save(createAbrangencia(kolaSanJon, ScopoTipo.ILHA, brava.getId(),          "Variante local sem umbigada, com cavalo e bandeira"));

        // ── Festa da Bandeira → Fogo ──────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festaBandeira, ScopoTipo.ILHA, fogo.getId(), "São Filipe, 30 Abril–1 Maio"));

        // ── Tabanka → Santiago ────────────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(tabanka, ScopoTipo.ILHA, santiago.getId(), "Maio/Junho. Associações Tabanka em vários concelhos."));

        // ── Carnaval de Mindelo → município São Vicente ───────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(carnavalMindelo, ScopoTipo.MUNICIPIO, mSaoVicente.getId(), "O maior carnaval do arquipélago. Desfile na Avenida Marginal."));

        // ── Carnaval da Praia → município Praia ──────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(carnavalPraia, ScopoTipo.MUNICIPIO, praia.getId(), "Forte influência africana. Grupos de mascarados e Batucadeiras."));

        // ── Batuque → Santiago ────────────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(batuque, ScopoTipo.ILHA, santiago.getId(), "Praticado em toda a ilha. Eventos em festas e comemorações."));

        // ── Festa de Santo António → Santo Antão, São Vicente, Santiago
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSantoAntonio, ScopoTipo.ILHA, santoAntao.getId(),     "Paúl é o centro das celebrações."));
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSantoAntonio, ScopoTipo.ILHA, saoVicenteIlha.getId(), "Festejado em Mindelo e arredores."));
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSantoAntonio, ScopoTipo.ILHA, santiago.getId(),       "Festejado em vários concelhos."));

        // ── Festival de Música da Boa Vista → Boa Vista ───────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festivalMusicaBV, ScopoTipo.ILHA, boaVista.getId(), "Realizado em Agosto. Artistas nacionais e internacionais."));

        // ── Festival de Batuku → Praia ────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festivalBatuku, ScopoTipo.MUNICIPIO, praia.getId(), "Bienal. Reúne grupos da diáspora e de África."));

        // ── Festa de NSra da Graça → todo o arquipélago ───────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festaNSGraca, ScopoTipo.NACIONAL, null, "Procissões marinhas e missas solenes em todo o arquipélago."));

        // ── Festa de São Pedro → Fogo, Santiago, Santo Antão ─────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSaoPedro, ScopoTipo.ILHA, fogo.getId(),     "Bênção dos barcos em São Filipe."));
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSaoPedro, ScopoTipo.ILHA, santiago.getId(), "Festejado nas comunidades piscatórias."));
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSaoPedro, ScopoTipo.ILHA, santoAntao.getId(), "Festejado nas comunidades piscatórias do norte."));

        // ── Festa de Finados → todo o arquipélago ─────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festaFinados, ScopoTipo.NACIONAL, null, "Tradição especialmente forte em Santiago."));

        // ── Festa de Santa Cruz → município Santa Cruz ────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(festaSantaCruz, ScopoTipo.MUNICIPIO, santaCruz.getId(), "Festa pagã com batizados simbólicos e procissões."));

        // ── Morna → todo o arquipélago ────────────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(morna, ScopoTipo.NACIONAL, null, "Património UNESCO 2019. Especialmente celebrada em Mindelo."));

        // ── Réveillon → todo o arquipélago ───────────────────────────
        feriadoAbrangenciaRepository.save(createAbrangencia(reveillon, ScopoTipo.NACIONAL, null, "Festas populares e fogos de artifício em todas as ilhas."));

        // ── Festa da Bandeira (Fogo) → município São Filipe ──────────
        // (abrangência municipal adicional para além da ilha)
        feriadoAbrangenciaRepository.save(createAbrangencia(festaBandeira, ScopoTipo.MUNICIPIO, mSaoFilipe.getId(), "Epicentro da festa. Ritual de pilagem de milho e tambores."));

        // ══════════════════════════════════════════════════════════════
        //  OCORRÊNCIA EXTRAORDINÁRIA — exemplo
        // ══════════════════════════════════════════════════════════════

        ocorrenciaExtraordinariaRepository.save(createOcorrenciaExtraordinaria(
            praia, "2026-11-14",
            "Feriado Municipal Extraordinário — Praia",
            "Celebração dos 30 anos da elevação a cidade",
            "Decreto Municipal CM/Praia/2026/xx",
            null));

        // ══════════════════════════════════════════════════════════════
        //  ENTIDADE DE TESTE
        // ══════════════════════════════════════════════════════════════

        TestEntity test = new TestEntity();
        test.setNome("Dados completos de Cabo Verde carregados");
        testEntityRepository.save(test);
    }

    // ══════════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════════

    private Santo createSanto(String nome, String nomeKriolu, Integer mes, Integer dia, String descricao) {
        Santo s = new Santo();
        s.setNome(nome);
        s.setNomeKriolu(nomeKriolu);
        s.setDiaFestivoMes(mes);
        s.setDiaFestivoDia(dia);
        s.setDescricao(descricao);
        return s;
    }

    private Ilha createIlha(String nome, String codigo) {
        Ilha i = new Ilha();
        i.setNome(nome);
        i.setCodigo(codigo);
        return i;
    }

    private Municipio createMunicipio(String nome, String codigo, Integer mes, Integer dia, Ilha ilha, Santo santo) {
        Municipio m = new Municipio();
        m.setNome(nome);
        m.setCodigo(codigo);
        m.setMesFeriado(mes);
        m.setDiaFeriado(dia);
        m.setIlha(ilha);
        m.setSantoPadroeiro(santo);
        return m;
    }

    private Feriado createFeriadoFixo(String nome, String nomeKriolu, TipoFeriado tipo,
            CategoriaFeriado categoria, Integer mes, Integer dia,
            String decreto, String descricao, Santo santo) {
        Feriado f = new Feriado();
        f.setNome(nome);
        f.setNomeKriolu(nomeKriolu);
        f.setTipo(tipo);
        f.setCategoria(categoria);
        f.setMesFixo(mes);
        f.setDiaFixo(dia);
        f.setMovel(false);
        f.setDecreto(decreto);
        f.setDescricao(descricao);
        f.setAtivo(true);
        f.setSanto(santo);
        return feriadoRepository.save(f);
    }

    private Feriado createFeriadoMovel(String nome, String nomeKriolu, TipoFeriado tipo,
            CategoriaFeriado categoria, RegraMovel regra,
            String decreto, String descricao, Santo santo) {
        Feriado f = new Feriado();
        f.setNome(nome);
        f.setNomeKriolu(nomeKriolu);
        f.setTipo(tipo);
        f.setCategoria(categoria);
        f.setMovel(true);
        f.setRegraMovel(regra);
        f.setDecreto(decreto);
        f.setDescricao(descricao);
        f.setAtivo(true);
        f.setSanto(santo);
        return feriadoRepository.save(f);
    }

    private FeriadoAbrangencia createAbrangencia(Feriado feriado, ScopoTipo scopoTipo,
            java.util.UUID scopeId, String nota) {
        FeriadoAbrangencia a = new FeriadoAbrangencia();
        a.setFeriado(feriado);
        a.setScopoTipo(scopoTipo);
        a.setScopeId(scopeId);
        a.setNota(nota);
        return a;
    }

    private OcorrenciaExtraordinaria createOcorrenciaExtraordinaria(Municipio municipio,
            String data, String nome, String motivo, String decreto, Feriado feriado) {
        OcorrenciaExtraordinaria o = new OcorrenciaExtraordinaria();
        o.setMunicipio(municipio);
        o.setData(LocalDate.parse(data));
        o.setNome(nome);
        o.setMotivo(motivo);
        o.setDecreto(decreto);
        o.setFeriado(feriado);
        return o;
    }
}