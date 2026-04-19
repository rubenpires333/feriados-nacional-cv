package feriados_nacionais.cv.dto;

import feriados_nacionais.cv.model.enums.CategoriaFeriado;
import feriados_nacionais.cv.model.enums.RegraMovel;
import feriados_nacionais.cv.model.enums.TipoFeriado;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FeriadoDTO {
    private UUID id;
    private String nome;
    private String nomeKriolu;
    private TipoFeriado tipo;
    private CategoriaFeriado categoria;
    private Integer mesFixo;
    private Integer diaFixo;
    private Boolean movel;
    private RegraMovel regraMovel;
    private String decreto;
    private String descricao;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    // Dados do santo (se houver)
    private SantoDTO santo;
}