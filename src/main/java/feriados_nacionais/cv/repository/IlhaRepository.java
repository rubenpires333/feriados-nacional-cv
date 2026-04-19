package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.Ilha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IlhaRepository extends JpaRepository<Ilha, UUID> {
    
    Optional<Ilha> findByCodigo(String codigo);
    
    Optional<Ilha> findByNome(String nome);
    
    List<Ilha> findAllByOrderByNome();
    
    boolean existsByCodigo(String codigo);
}