package feriados_nacionais.cv.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import feriados_nacionais.cv.model.enums.CategoriaFeriado;
import feriados_nacionais.cv.model.enums.RegraMovel;
import feriados_nacionais.cv.model.enums.TipoFeriado;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "feriado")
@Data
@EqualsAndHashCode(of = "id")
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "santo_id")
    private Santo santo;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "nome_kriolu", length = 150)
    private String nomeKriolu;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoFeriado tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaFeriado categoria;

    @Column(name = "mes_fixo")
    private Integer mesFixo;

    @Column(name = "dia_fixo")
    private Integer diaFixo;

    @Column(name = "movel", nullable = false)
    private Boolean movel = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "regra_movel")
    private RegraMovel regraMovel;

    @Column(name = "decreto", length = 100)
    private String decreto;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "feriado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FeriadoAbrangencia> abrangencias;

    // Constraint check será implementada via @PrePersist e @PreUpdate
    @PrePersist
    @PreUpdate
    private void validarDataOuMovel() {
        // Se movel é null, assumir false (feriado fixo)
        if (movel == null) {
            movel = false;
        }
        
        if (movel) {
            // Se é móvel, não deve ter data fixa
            if (mesFixo != null || diaFixo != null) {
                throw new IllegalStateException("Feriado móvel não pode ter data fixa");
            }
            if (regraMovel == null) {
                throw new IllegalStateException("Feriado móvel deve ter regra de cálculo");
            }
        } else {
            // Se não é móvel, deve ter data fixa
            if (mesFixo == null || diaFixo == null) {
                throw new IllegalStateException("Feriado fixo deve ter data definida");
            }
            if (regraMovel != null) {
                throw new IllegalStateException("Feriado fixo não pode ter regra móvel");
            }
        }
    }
}