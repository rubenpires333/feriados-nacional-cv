package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.OcorrenciaExtraordinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OcorrenciaExtraordinariaRepository extends JpaRepository<OcorrenciaExtraordinaria, UUID> {
    
    List<OcorrenciaExtraordinaria> findByMunicipioId(UUID municipioId);
    
    List<OcorrenciaExtraordinaria> findByData(LocalDate data);
    
    List<OcorrenciaExtraordinaria> findByDataBetween(LocalDate startDate, LocalDate endDate);
}