package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.Feriado;
import feriados_nacionais.cv.model.enums.TipoFeriado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, UUID> {

    List<Feriado> findByAtivoTrue();
    
    Page<Feriado> findByAtivoTrue(Pageable pageable);
    
    List<Feriado> findByTipoAndAtivoTrue(TipoFeriado tipo);
    
    List<Feriado> findByMovelAndAtivoTrue(Boolean movel);
    
    Optional<Feriado> findByNomeAndAtivoTrue(String nome);
    
    @Query("SELECT f FROM Feriado f WHERE f.ativo = true AND f.movel = false ORDER BY f.mesFixo, f.diaFixo")
    List<Feriado> findFeriadosFixosAtivos();
    
    @Query("SELECT f FROM Feriado f WHERE f.ativo = true AND f.movel = true")
    List<Feriado> findFeriadosMoveis();
    
    @Query("SELECT f FROM Feriado f JOIN f.abrangencias a WHERE a.scopoTipo = 'NACIONAL' AND f.ativo = true")
    List<Feriado> findFeriadosNacionais();
    
    @Query("SELECT f FROM Feriado f JOIN f.abrangencias a WHERE a.scopoTipo = 'NACIONAL' AND f.ativo = true ORDER BY f.mesFixo, f.diaFixo")
    List<Feriado> findFeriadosNacionaisAtivos();
    
    @Query("SELECT f FROM Feriado f WHERE f.ativo = true AND f.movel = false AND f.mesFixo = :mes AND f.diaFixo = :dia")
    List<Feriado> findFeriadosFixosByMesAndDia(@Param("mes") Integer mes, @Param("dia") Integer dia);
    
    @Query("SELECT f FROM Feriado f JOIN f.abrangencias a JOIN Ilha i ON i.id = a.scopeId " +
           "WHERE a.scopoTipo = 'ILHA' AND i.codigo = :codigoIlha AND f.ativo = true")
    List<Feriado> findFeriadosByIlhaCodigo(@Param("codigoIlha") String codigoIlha);
    
    @Query("SELECT f FROM Feriado f JOIN f.abrangencias a JOIN Municipio m ON m.id = a.scopeId " +
           "WHERE a.scopoTipo = 'MUNICIPIO' AND m.codigo = :codigoMunicipio AND f.ativo = true")
    List<Feriado> findFeriadosByMunicipioCodigo(@Param("codigoMunicipio") String codigoMunicipio);
}