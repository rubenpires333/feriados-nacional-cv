package feriados_nacionais.cv.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SantoDTO {
    private UUID id;
    private String nome;
    private String nomeKriolu;
    private Integer diaFestivoMes;
    private Integer diaFestivoDia;
    private String descricao;
}