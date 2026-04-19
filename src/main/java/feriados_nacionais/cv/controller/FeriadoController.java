package feriados_nacionais.cv.controller;

import feriados_nacionais.cv.service.CalculoPascoaService;
import feriados_nacionais.cv.service.FeriadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Feriados de Cabo Verde", description = "API para consulta de feriados (requer autenticação)")
public class FeriadoController {

    @Autowired
    private FeriadoService feriadoService;

    @Autowired
    private CalculoPascoaService calculoPascoaService;

    // ═══════════════════════════════════════════════════════════════
    // CONSULTA PRINCIPAL - ESSENCIAL
    // ═══════════════════════════════════════════════════════════════

    @Operation(summary = "Lista de feriados", 
               description = "Lista feriados por ano, município, ilha ou tipo com datas calculadas. Sem parâmetros retorna ano corrente e nacionais.")
    @GetMapping("/feriados")
    public ResponseEntity<List<Map<String, Object>>> listarFeriados(
            @Parameter(description = "Ano (padrão: ano corrente)") @RequestParam(required = false) Integer ano,
            @Parameter(description = "Código do município") @RequestParam(required = false) String municipio,
            @Parameter(description = "Código da ilha") @RequestParam(required = false) String ilha,
            @Parameter(description = "Tipo: NACIONAL, TRADICIONAL, COMEMORATIVO") @RequestParam(required = false) String tipo) {
        
        // Se não especificar ano, usar ano corrente
        if (ano == null) ano = LocalDate.now().getYear();
        
        return ResponseEntity.ok(feriadoService.listarFeriadosSimplificado(ano, municipio, ilha, tipo));
    }

    @Operation(summary = "Feriados de um ano específico", 
               description = "Todos os feriados nacionais de um ano específico com datas calculadas.")
    @GetMapping("/feriados/{ano}")
    public ResponseEntity<List<Map<String, Object>>> feriadosDoAno(
            @Parameter(description = "Ano") @PathVariable Integer ano) {
        
        return ResponseEntity.ok(feriadoService.feriadosDoAno(ano));
    }

    // ═══════════════════════════════════════════════════════════════
    // VERIFICAÇÃO DE DATAS - ESSENCIAL
    // ═══════════════════════════════════════════════════════════════

    @Operation(summary = "Feriados de hoje", 
               description = "Retorna os feriados ativos hoje com datas para dashboards e sistemas de presença.")
    @GetMapping("/feriados/hoje")
    public ResponseEntity<List<Map<String, Object>>> feriadosHoje(
            @Parameter(description = "Código do município (opcional)") @RequestParam(required = false) String municipio) {
        
        return ResponseEntity.ok(feriadoService.feriadosHoje(municipio));
    }

    @Operation(summary = "Verificar se é feriado", 
               description = "Verifica se uma data é feriado. Retorna {feriado: true/false, detalhes}.")
    @GetMapping("/feriados/verificar/{data}")
    public ResponseEntity<Map<String, Object>> verificarFeriado(
            @Parameter(description = "Data no formato YYYY-MM-DD") @PathVariable String data,
            @Parameter(description = "Código do município (opcional)") @RequestParam(required = false) String municipio) {
        
        return ResponseEntity.ok(feriadoService.verificarFeriado(data, municipio));
    }

    // ═══════════════════════════════════════════════════════════════
    // CÁLCULO DE PÁSCOA - ÚTIL
    // ═══════════════════════════════════════════════════════════════

    @Operation(summary = "Cálculo da Páscoa e feriados móveis", 
               description = "Data da Páscoa e todos os feriados móveis derivados para o ano.")
    @GetMapping("/pascoa/{ano}")
    public ResponseEntity<Map<String, Object>> calcularPascoa(
            @Parameter(description = "Ano") @PathVariable Integer ano) {
        
        LocalDate pascoa = calculoPascoaService.calcularPascoa(ano);
        
        return ResponseEntity.ok(Map.of(
            "ano", ano,
            "pascoa", pascoa,
            "carnaval", calculoPascoaService.calcularFeriadoMovel(ano, -47),
            "quartaCinzas", calculoPascoaService.calcularFeriadoMovel(ano, -46),
            "sextaSanta", calculoPascoaService.calcularFeriadoMovel(ano, -2)
        ));
    }

    // ═══════════════════════════════════════════════════════════════
    // UTILITÁRIOS ADICIONAIS
    // ═══════════════════════════════════════════════════════════════

    @Operation(summary = "Próximos feriados", 
               description = "Lista os próximos N feriados a partir de hoje com dias restantes.")
    @GetMapping("/feriados/proximos")
    public ResponseEntity<List<Map<String, Object>>> proximosFeriados(
            @Parameter(description = "Quantidade de feriados (padrão: 5)") @RequestParam(defaultValue = "5") Integer n,
            @Parameter(description = "Código do município (opcional)") @RequestParam(required = false) String municipio) {
        
        return ResponseEntity.ok(feriadoService.proximosFeriados(n, municipio));
    }

    @Operation(summary = "Contar dias úteis", 
               description = "Conta dias úteis entre duas datas, excluindo feriados e fins-de-semana.")
    @GetMapping("/feriados/dias-uteis")
    public ResponseEntity<Map<String, Object>> contarDiasUteis(
            @Parameter(description = "Data de início (YYYY-MM-DD)") @RequestParam String inicio,
            @Parameter(description = "Data de fim (YYYY-MM-DD)") @RequestParam String fim,
            @Parameter(description = "Código do município (opcional)") @RequestParam(required = false) String municipio) {
        
        return ResponseEntity.ok(feriadoService.contarDiasUteis(inicio, fim, municipio));
    }
}