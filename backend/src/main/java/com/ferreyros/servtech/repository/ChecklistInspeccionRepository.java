package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.ChecklistInspeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistInspeccionRepository extends JpaRepository<ChecklistInspeccion, Integer> {
    Optional<ChecklistInspeccion> findByOrden_IdOrden(Integer idOrden);
}
