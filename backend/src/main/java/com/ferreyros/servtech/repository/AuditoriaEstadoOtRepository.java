package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.AuditoriaEstadoOt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaEstadoOtRepository extends JpaRepository<AuditoriaEstadoOt, Long> {

    @Query("SELECT DISTINCT a FROM AuditoriaEstadoOt a " +
           "LEFT JOIN FETCH a.orden o " +
           "LEFT JOIN FETCH o.maquinaria m " +
           "LEFT JOIN FETCH o.sede s " +
           "LEFT JOIN FETCH a.cambiadoPor u " +
           "WHERE o.idOrden = :idOrden ORDER BY a.fechaCambio DESC")
    List<AuditoriaEstadoOt> findByOrden_IdOrdenOrderByFechaCambioDesc(@Param("idOrden") Integer idOrden);

    @Query("SELECT DISTINCT a FROM AuditoriaEstadoOt a " +
           "LEFT JOIN FETCH a.orden o " +
           "LEFT JOIN FETCH o.maquinaria m " +
           "LEFT JOIN FETCH o.sede s " +
           "LEFT JOIN FETCH a.cambiadoPor u " +
           "ORDER BY a.fechaCambio DESC")
    List<AuditoriaEstadoOt> findAllConRelaciones();

    List<AuditoriaEstadoOt> findAllByOrderByFechaCambioDesc();
}
