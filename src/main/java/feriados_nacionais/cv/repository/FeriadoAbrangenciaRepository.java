package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.FeriadoAbrangencia;
import feriados_nacionais.cv.model.enums.ScopoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeriadoAbrangenciaRepository extends JpaRepository<FeriadoAbrangencia, UUID> {
    
    List<FeriadoAbrangencia> findByFeriadoId(UUID feriadoId);
    
    List<FeriadoAbrangencia> findByScopoTipo(ScopoTipo scopoTipo);
    
    List<FeriadoAbrangencia> findByScopoTipoAndScopeId(ScopoTipo scopoTipo, UUID scopeId);
}