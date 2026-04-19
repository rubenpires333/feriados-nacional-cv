package feriados_nacionais.cv.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "santo")
@Data
@EqualsAndHashCode(of = "id")
public class Santo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "nome_kriolu", length = 100)
    private String nomeKriolu;

    @Column(name = "dia_festivo_mes")
    private Integer diaFestivoMes;

    @Column(name = "dia_festivo_dia")
    private Integer diaFestivoDia;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}