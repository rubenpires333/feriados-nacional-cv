package feriados_nacionais.cv.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "municipio")
@Data
@EqualsAndHashCode(of = "id")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ilha_id", nullable = false)
    private Ilha ilha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "santo_padroeiro_id")
    private Santo santoPadroeiro;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "mes_feriado")
    private Integer mesFeriado;

    @Column(name = "dia_feriado")
    private Integer diaFeriado;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}