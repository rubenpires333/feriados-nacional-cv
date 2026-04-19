package feriados_nacionais.cv.model.entity;

import feriados_nacionais.cv.model.enums.ScopoTipo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feriado_abrangencia", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"feriado_id", "scope_tipo", "scope_id"}))
@Data
@EqualsAndHashCode(of = "id")
public class FeriadoAbrangencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feriado_id", nullable = false)
    private Feriado feriado;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_tipo", nullable = false)
    private ScopoTipo scopoTipo;

    @Column(name = "scope_id")
    private UUID scopeId;

    @Column(name = "nota", columnDefinition = "TEXT")
    private String nota;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}