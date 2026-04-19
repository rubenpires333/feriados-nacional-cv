package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, UUID> {
    
    Optional<Municipio> findByCodigo(String codigo);
    
    List<Municipio> findByIlhaId(UUID ilhaId);
    
    List<Municipio> findByIlhaIdOrderByNome(UUID ilhaId);
    
    List<Municipio> findAllByOrderByNome();
    
    boolean existsByCodigo(String codigo);
}