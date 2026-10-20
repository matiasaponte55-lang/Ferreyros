package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.AuditoriaEstadoOt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaEstadoOtRepository extends JpaRepository<AuditoriaEstadoOt, Long> {
    List<AuditoriaEstadoOt> findByOrden_IdOrdenOrderByFechaCambioDesc(Integer idOrden);
    List<AuditoriaEstadoOt> findAllByOrderByFechaCambioDesc();
}
