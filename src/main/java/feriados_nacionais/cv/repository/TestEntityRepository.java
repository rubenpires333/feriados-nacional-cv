package feriados_nacionais.cv.repository;

import feriados_nacionais.cv.model.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, UUID> {
}