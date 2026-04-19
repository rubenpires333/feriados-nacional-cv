package feriados_nacionais.cv.service;

import feriados_nacionais.cv.dto.FeriadoDTO;
import feriados_nacionais.cv.dto.PagedResponseDTO;
import feriados_nacionais.cv.dto.SantoDTO;
import feriados_nacionais.cv.model.entity.Feriado;
import feriados_nacionais.cv.model.enums.TipoFeriado;
import feriados_nacionais.cv.repository.FeriadoRepository;
import feriados_nacionais.cv.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FeriadoService {

    @Autowired
    private FeriadoRepository feriadoRepository;

    @Autowired
    private CalculoPascoaService calculoPascoaService;

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS SIMPLIFICADOS PARA A NOVA API
    // ═══════════════════════════════════════════════════════════════

    public List<Map<String, Object>> listarFeriadosSimplificado(Integer ano, String municipio, String ilha, String tipo) {
        List<Feriado> feriados;
        
        // Filtrar por tipo se especificado
        if (tipo != null) {
            try {
                TipoFeriado tipoEnum = TipoFeriado.valueOf(tipo.toUpperCase());
                feriados = feriadoRepository.findByTipoAndAtivoTrue(tipoEnum);
            } catch (IllegalArgumentException e) {
                feriados = feriadoRepository.findByAtivoTrue();
            }
        } else {
            feriados = feriadoRepository.findByAtivoTrue();
        }
        
        // Filtrar por município se especificado
        if (municipio != null && !municipio.trim().isEmpty()) {
            List<Feriado> feriadosMunicipio = feriadoRepository.findFeriadosByMunicipioCodigo(municipio.toLowerCase());
            // Combinar feriados nacionais com municipais
            List<Feriado> feriadosNacionais = feriadoRepository.findFeriadosNacionaisAtivos();
            feriados = new java.util.ArrayList<>(feriadosNacionais);
            feriados.addAll(feriadosMunicipio);
        }
        
        // Filtrar por ilha se especificado
        if (ilha != null && !ilha.trim().isEmpty()) {
            List<Feriado> feriadosIlha = feriadoRepository.findFeriadosByIlhaCodigo(ilha.toUpperCase());
            // Combinar feriados nacionais com da ilha
            List<Feriado> feriadosNacionais = feriadoRepository.findFeriadosNacionaisAtivos();
            feriados = new java.util.ArrayList<>(feriadosNacionais);
            feriados.addAll(feriadosIlha);
        }
        
        return feriados.stream()
                .distinct() // Remover duplicatas
                .map(feriado -> convertToSimpleMap(feriado, ano))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> feriadosDoAno(Integer ano) {
        List<Feriado> feriados = feriadoRepository.findFeriadosNacionaisAtivos();
        
        return feriados.stream()
                .map(feriado -> convertToSimpleMapWithDate(feriado, ano))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> feriadosHoje(String municipio) {
        LocalDate hoje = LocalDate.now();
        List<Feriado> feriados = new java.util.ArrayList<>();
        
        // Buscar feriados fixos para hoje
        List<Feriado> feriadosFixos = feriadoRepository.findFeriadosFixosByMesAndDia(hoje.getMonthValue(), hoje.getDayOfMonth());
        feriados.addAll(feriadosFixos);
        
        // Buscar feriados móveis para hoje
        List<Feriado> feriadosMoveis = feriadoRepository.findFeriadosMoveis();
        for (Feriado feriado : feriadosMoveis) {
            if (feriado.getRegraMovel() != null) {
                LocalDate dataFeriado = calculoPascoaService.calcularFeriadoMovel(hoje.getYear(), feriado.getRegraMovel().getDiasDaPascoa());
                if (dataFeriado.equals(hoje)) {
                    feriados.add(feriado);
                }
            }
        }
        
        // Filtrar por município se especificado
        if (municipio != null && !municipio.trim().isEmpty()) {
            List<Feriado> feriadosMunicipio = feriadoRepository.findFeriadosByMunicipioCodigo(municipio.toLowerCase());
            // Adicionar feriados municipais que coincidem com hoje
            for (Feriado feriado : feriadosMunicipio) {
                if (!feriado.getMovel() && feriado.getMesFixo() != null && feriado.getDiaFixo() != null) {
                    if (feriado.getMesFixo() == hoje.getMonthValue() && feriado.getDiaFixo() == hoje.getDayOfMonth()) {
                        feriados.add(feriado);
                    }
                }
            }
        }
        
        return feriados.stream()
                .distinct()
                .map(feriado -> convertToSimpleMap(feriado, hoje.getYear()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> verificarFeriado(String data, String municipio) {
        try {
            LocalDate date = LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE);
            List<Feriado> feriadosEncontrados = new java.util.ArrayList<>();
            
            // Verificar feriados fixos
            List<Feriado> feriadosFixos = feriadoRepository.findFeriadosFixosByMesAndDia(date.getMonthValue(), date.getDayOfMonth());
            feriadosEncontrados.addAll(feriadosFixos);
            
            // Verificar feriados móveis
            List<Feriado> feriadosMoveis = feriadoRepository.findFeriadosMoveis();
            for (Feriado feriado : feriadosMoveis) {
                if (feriado.getRegraMovel() != null) {
                    LocalDate dataFeriado = calculoPascoaService.calcularFeriadoMovel(date.getYear(), feriado.getRegraMovel().getDiasDaPascoa());
                    if (dataFeriado.equals(date)) {
                        feriadosEncontrados.add(feriado);
                    }
                }
            }
            
            // Verificar feriados municipais se especificado
            if (municipio != null && !municipio.trim().isEmpty()) {
                List<Feriado> feriadosMunicipio = feriadoRepository.findFeriadosByMunicipioCodigo(municipio.toLowerCase());
                for (Feriado feriado : feriadosMunicipio) {
                    if (!feriado.getMovel() && feriado.getMesFixo() != null && feriado.getDiaFixo() != null) {
                        if (feriado.getMesFixo() == date.getMonthValue() && feriado.getDiaFixo() == date.getDayOfMonth()) {
                            feriadosEncontrados.add(feriado);
                        }
                    }
                }
            }
            
            boolean isFeriado = !feriadosEncontrados.isEmpty();
            boolean isFimDeSemana = date.getDayOfWeek().getValue() >= 6; // Sábado = 6, Domingo = 7
            
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("feriado", isFeriado);
            result.put("data", data);
            result.put("diaSemana", DateUtils.getDiaSemanaPortugues(date.getDayOfWeek()));
            result.put("fimDeSemana", isFimDeSemana);
            result.put("diaUtil", !isFeriado && !isFimDeSemana);
            
            if (isFeriado) {
                result.put("detalhes", feriadosEncontrados.stream()
                    .distinct()
                    .map(f -> {
                        Map<String, Object> detalhe = new java.util.HashMap<>();
                        detalhe.put("nome", f.getNome());
                        detalhe.put("tipo", f.getTipo().toString());
                        detalhe.put("categoria", f.getCategoria().toString());
                        if (f.getNomeKriolu() != null) {
                            detalhe.put("nomeKriolu", f.getNomeKriolu());
                        }
                        return detalhe;
                    })
                    .collect(Collectors.toList()));
            }
            
            return result;
        } catch (Exception e) {
            return Map.of("erro", "Data inválida: " + e.getMessage(), "feriado", false);
        }
    }

    private Map<String, Object> convertToSimpleMap(Feriado feriado, Integer ano) {
        Map<String, Object> map = new java.util.HashMap<>();
        // Removido o ID conforme solicitado
        map.put("nome", feriado.getNome());
        map.put("tipo", feriado.getTipo().toString());
        map.put("categoria", feriado.getCategoria().toString());
        
        if (feriado.getNomeKriolu() != null) {
            map.put("nomeKriolu", feriado.getNomeKriolu());
        }
        
        // SEMPRE incluir a data calculada
        if (feriado.getMovel()) {
            if (feriado.getRegraMovel() != null) {
                LocalDate data = calculoPascoaService.calcularFeriadoMovel(ano, feriado.getRegraMovel().getDiasDaPascoa());
                map.put("data", data);
                map.put("diaSemana", DateUtils.getDiaSemanaPortugues(data.getDayOfWeek()));
            }
        } else {
            if (feriado.getMesFixo() != null && feriado.getDiaFixo() != null) {
                LocalDate data = LocalDate.of(ano, feriado.getMesFixo(), feriado.getDiaFixo());
                map.put("data", data);
                map.put("diaSemana", DateUtils.getDiaSemanaPortugues(data.getDayOfWeek()));
            }
        }
        
        // Adicionar informações úteis
        map.put("movel", feriado.getMovel());
        if (feriado.getDescricao() != null && !feriado.getDescricao().trim().isEmpty()) {
            map.put("descricao", feriado.getDescricao());
        }
        
        return map;
    }

    private Map<String, Object> convertToSimpleMapWithDate(Feriado feriado, Integer ano) {
        // Agora convertToSimpleMap já inclui a data, então podemos usar o mesmo método
        return convertToSimpleMap(feriado, ano);
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS UTILITÁRIOS ADICIONAIS
    // ═══════════════════════════════════════════════════════════════

    public Map<String, Object> contarDiasUteis(String dataInicio, String dataFim, String municipio) {
        try {
            LocalDate inicio = LocalDate.parse(dataInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fim = LocalDate.parse(dataFim, DateTimeFormatter.ISO_LOCAL_DATE);
            
            if (inicio.isAfter(fim)) {
                return Map.of("erro", "Data de início deve ser anterior à data de fim", "diasUteis", 0);
            }
            
            int diasUteis = 0;
            int diasCorridos = 0;
            int feriados = 0;
            int finsDeSemana = 0;
            
            LocalDate atual = inicio;
            while (!atual.isAfter(fim)) {
                diasCorridos++;
                
                boolean isFimDeSemana = atual.getDayOfWeek().getValue() >= 6;
                boolean isFeriado = !verificarFeriado(atual.toString(), municipio).get("feriado").equals(false);
                
                if (isFimDeSemana) {
                    finsDeSemana++;
                } else if (isFeriado) {
                    feriados++;
                } else {
                    diasUteis++;
                }
                
                atual = atual.plusDays(1);
            }
            
            Map<String, Object> resultado = new java.util.HashMap<>();
            resultado.put("dataInicio", dataInicio);
            resultado.put("dataFim", dataFim);
            resultado.put("diasCorridos", diasCorridos);
            resultado.put("diasUteis", diasUteis);
            resultado.put("feriados", feriados);
            resultado.put("finsDeSemana", finsDeSemana);
            
            return resultado;
            
        } catch (Exception e) {
            return Map.of("erro", "Datas inválidas: " + e.getMessage(), "diasUteis", 0);
        }
    }

    public List<Map<String, Object>> proximosFeriados(Integer quantidade, String municipio) {
        LocalDate hoje = LocalDate.now();
        List<Map<String, Object>> proximosFeriados = new java.util.ArrayList<>();
        
        // Buscar feriados do ano atual e próximo
        for (int ano = hoje.getYear(); ano <= hoje.getYear() + 1; ano++) {
            List<Map<String, Object>> feriadosAno = feriadosDoAno(ano);
            
            for (Map<String, Object> feriado : feriadosAno) {
                LocalDate dataFeriado = (LocalDate) feriado.get("data");
                if (dataFeriado != null && dataFeriado.isAfter(hoje)) {
                    Map<String, Object> feriadoComDias = new java.util.HashMap<>(feriado);
                    feriadoComDias.put("diasRestantes", java.time.temporal.ChronoUnit.DAYS.between(hoje, dataFeriado));
                    proximosFeriados.add(feriadoComDias);
                }
            }
        }
        
        // Ordenar por data e limitar quantidade
        return proximosFeriados.stream()
                .sorted((f1, f2) -> ((LocalDate) f1.get("data")).compareTo((LocalDate) f2.get("data")))
                .limit(quantidade != null ? quantidade : 5)
                .collect(Collectors.toList());
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS ORIGINAIS (MANTIDOS PARA COMPATIBILIDADE)
    // ═══════════════════════════════════════════════════════════════

    public PagedResponseDTO<FeriadoDTO> listarFeriados(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Feriado> feriadosPage = feriadoRepository.findAll(pageable);
        List<FeriadoDTO> feriadosDTO = feriadosPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PagedResponseDTO<>(
            feriadosDTO,
            feriadosPage.getNumber(),
            feriadosPage.getSize(),
            feriadosPage.getTotalElements()
        );
    }

    public PagedResponseDTO<FeriadoDTO> listarFeriadosAtivos(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Feriado> feriadosPage = feriadoRepository.findByAtivoTrue(pageable);
        List<FeriadoDTO> feriadosDTO = feriadosPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PagedResponseDTO<>(
            feriadosDTO,
            feriadosPage.getNumber(),
            feriadosPage.getSize(),
            feriadosPage.getTotalElements()
        );
    }

    public List<FeriadoDTO> listarFeriadosNacionais() {
        List<Feriado> feriados = feriadoRepository.findFeriadosNacionais();
        return feriados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FeriadoDTO> listarFeriadosFixos() {
        List<Feriado> feriados = feriadoRepository.findFeriadosFixosAtivos();
        return feriados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FeriadoDTO> listarFeriadosMoveis() {
        List<Feriado> feriados = feriadoRepository.findFeriadosMoveis();
        return feriados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FeriadoDTO> buscarPorId(UUID id) {
        return feriadoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<FeriadoDTO> buscarPorNome(String nome) {
        return feriadoRepository.findByNomeAndAtivoTrue(nome)
                .map(this::convertToDTO);
    }

    public List<FeriadoDTO> listarFeriadosPorIlha(String codigoIlha) {
        List<Feriado> feriados = feriadoRepository.findFeriadosByIlhaCodigo(codigoIlha);
        return feriados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FeriadoDTO> listarFeriadosPorMunicipio(String codigoMunicipio) {
        List<Feriado> feriados = feriadoRepository.findFeriadosByMunicipioCodigo(codigoMunicipio);
        return feriados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FeriadoDTO convertToDTO(Feriado feriado) {
        FeriadoDTO dto = new FeriadoDTO();
        dto.setId(feriado.getId());
        dto.setNome(feriado.getNome());
        dto.setNomeKriolu(feriado.getNomeKriolu());
        dto.setTipo(feriado.getTipo());
        dto.setCategoria(feriado.getCategoria());
        dto.setMesFixo(feriado.getMesFixo());
        dto.setDiaFixo(feriado.getDiaFixo());
        dto.setMovel(feriado.getMovel());
        dto.setRegraMovel(feriado.getRegraMovel());
        dto.setDecreto(feriado.getDecreto());
        dto.setDescricao(feriado.getDescricao());
        dto.setAtivo(feriado.getAtivo());
        dto.setCriadoEm(feriado.getCriadoEm());
        dto.setAtualizadoEm(feriado.getAtualizadoEm());
        
        // Converter santo se existir
        if (feriado.getSanto() != null) {
            SantoDTO santoDTO = new SantoDTO();
            santoDTO.setId(feriado.getSanto().getId());
            santoDTO.setNome(feriado.getSanto().getNome());
            santoDTO.setNomeKriolu(feriado.getSanto().getNomeKriolu());
            santoDTO.setDescricao(feriado.getSanto().getDescricao());
            dto.setSanto(santoDTO);
        }
        
        return dto;
    }
}