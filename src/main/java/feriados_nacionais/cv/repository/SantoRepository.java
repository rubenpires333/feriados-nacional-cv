package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.Santo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SantoRepository extends JpaRepository<Santo, UUID> {
    
    Optional<Santo> findByNome(String nome);
    
    List<Santo> findAllByOrderByNome();
    
    boolean existsByNome(String nome);
}