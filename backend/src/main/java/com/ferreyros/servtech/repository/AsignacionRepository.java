package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {

    @Query("SELECT DISTINCT a FROM Asignacion a " +
           "LEFT JOIN FETCH a.orden o " +
           "LEFT JOIN FETCH o.maquinaria " +
           "LEFT JOIN FETCH a.tecnico t " +
           "LEFT JOIN FETCH t.usuario " +
           "WHERE o.idOrden = :idOrden")
    List<Asignacion> findByOrdenConRelaciones(@Param("idOrden") Integer idOrden);

    @Query("SELECT DISTINCT a FROM Asignacion a " +
           "LEFT JOIN FETCH a.orden o " +
           "LEFT JOIN FETCH o.maquinaria " +
           "LEFT JOIN FETCH a.tecnico t " +
           "LEFT JOIN FETCH t.usuario " +
           "WHERE t.idTecnico = :idTecnico")
    List<Asignacion> findByTecnicoConRelaciones(@Param("idTecnico") Integer idTecnico);

    List<Asignacion> findByOrden_IdOrden(Integer idOrden);

    List<Asignacion> findByTecnico_IdTecnico(Integer idTecnico);

    /* Regla Anti-Solapamiento GiST  */
    @Query("SELECT a FROM Asignacion a WHERE a.tecnico.idTecnico = :idTecnico " +
           "AND a.estadoAsignacion != 'CANCELADA' " +
           "AND (a.fechaInicio < :fin AND a.fechaFin > :inicio)")
    List<Asignacion> findOverlappingAssignments(
            @Param("idTecnico") Integer idTecnico,
            @Param("inicio") OffsetDateTime inicio,
            @Param("fin") OffsetDateTime fin
    );
}
