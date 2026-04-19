package feriados_nacionais.cv.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ocorrencia_extraordinaria")
@Data
@EqualsAndHashCode(of = "id")
public class OcorrenciaExtraordinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feriado_id")
    private Feriado feriado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "decreto", length = 150)
    private String decreto;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}