package feriados_nacionais.cv.controller;

import feriados_nacionais.cv.model.entity.Ilha;
import feriados_nacionais.cv.model.entity.Municipio;
import feriados_nacionais.cv.model.entity.Santo;
import feriados_nacionais.cv.repository.IlhaRepository;
import feriados_nacionais.cv.repository.MunicipioRepository;
import feriados_nacionais.cv.repository.SantoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Referência Geográfica", description = "Ilhas, municípios e santos de Cabo Verde (requer autenticação)")
public class LocalidadeController {

    @Autowired
    private IlhaRepository ilhaRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private SantoRepository santoRepository;

    @Operation(summary = "Lista de ilhas", description = "Lista todas as ilhas com código ISO e número de municípios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ilhas")
    })
    @GetMapping("/ilhas")
    public ResponseEntity<List<Map<String, Object>>> listarIlhas() {
        List<Ilha> ilhas = ilhaRepository.findAllByOrderByNome();
        List<Map<String, Object>> response = ilhas.stream()
                .map(ilha -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("nome", ilha.getNome());
                    map.put("codigo", ilha.getCodigo());
                    map.put("municipios", ilha.getMunicipios() != null ? ilha.getMunicipios().size() : 0);
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista de municípios", description = "Lista todos os municípios. Filtra por ilha com ?ilha=codigo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de municípios")
    })
    @GetMapping("/municipios")
    public ResponseEntity<List<Map<String, Object>>> listarMunicipios(
            @Parameter(description = "Código da ilha (opcional)", example = "ST")
            @RequestParam(required = false) String ilha) {
        
        List<Municipio> municipios;
        
        if (ilha != null && !ilha.trim().isEmpty()) {
            Optional<Ilha> ilhaEntity = ilhaRepository.findByCodigo(ilha.toUpperCase());
            if (ilhaEntity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            municipios = municipioRepository.findByIlhaIdOrderByNome(ilhaEntity.get().getId());
        } else {
            municipios = municipioRepository.findAllByOrderByNome();
        }
        
        List<Map<String, Object>> response = municipios.stream()
                .map(municipio -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("nome", municipio.getNome());
                    map.put("codigo", municipio.getCodigo());
                    map.put("ilha", municipio.getIlha().getNome());
                    map.put("ilhaCodigo", municipio.getIlha().getCodigo());
                    if (municipio.getSantoPadroeiro() != null) {
                        map.put("santoPadroeiro", municipio.getSantoPadroeiro().getNome());
                    }
                    return map;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Detalhes de um município", description = "Detalhe de um município com ilha, santo padroeiro e data do feriado municipal")
    @GetMapping("/municipios/{codigo}")
    public ResponseEntity<Map<String, Object>> detalhesMunicipio(
            @Parameter(description = "Código do município") @PathVariable String codigo) {
        
        Optional<Municipio> municipio = municipioRepository.findByCodigo(codigo.toLowerCase());
        if (municipio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Municipio m = municipio.get();
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("nome", m.getNome());
        response.put("codigo", m.getCodigo());
        response.put("ilha", Map.of(
            "nome", m.getIlha().getNome(),
            "codigo", m.getIlha().getCodigo()
        ));
        
        if (m.getSantoPadroeiro() != null) {
            response.put("santoPadroeiro", Map.of(
                "nome", m.getSantoPadroeiro().getNome(),
                "nomeKriolu", m.getSantoPadroeiro().getNomeKriolu(),
                "diaFestivo", m.getSantoPadroeiro().getDiaFestivoDia() + "/" + m.getSantoPadroeiro().getDiaFestivoMes()
            ));
        }
        
        if (m.getMesFeriado() != null && m.getDiaFeriado() != null) {
            response.put("feriadoMunicipal", m.getDiaFeriado() + "/" + m.getMesFeriado());
        }
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista de santos", description = "Lista todos os santos registados com dia festivo e municípios onde são padroeiros")
    @GetMapping("/santos")
    public ResponseEntity<List<Map<String, Object>>> listarSantos() {
        List<Santo> santos = santoRepository.findAllByOrderByNome();
        List<Map<String, Object>> response = santos.stream()
                .map(santo -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("nome", santo.getNome());
                    map.put("nomeKriolu", santo.getNomeKriolu());
                    if (santo.getDiaFestivoMes() != null && santo.getDiaFestivoDia() != null) {
                        map.put("diaFestivo", santo.getDiaFestivoDia() + "/" + santo.getDiaFestivoMes());
                    }
                    map.put("descricao", santo.getDescricao());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}