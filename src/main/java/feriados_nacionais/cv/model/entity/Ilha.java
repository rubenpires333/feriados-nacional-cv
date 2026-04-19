package feriados_nacionais.cv.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ilha")
@Data
@EqualsAndHashCode(of = "id")
public class Ilha {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true, length = 60)
    private String nome;

    @Column(name = "codigo", nullable = false, unique = true, length = 3)
    private String codigo;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "ilha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Municipio> municipios;
}