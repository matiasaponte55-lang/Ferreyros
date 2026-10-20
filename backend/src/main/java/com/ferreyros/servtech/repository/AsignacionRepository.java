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

    List<Asignacion> findByOrden_IdOrden(Integer idOrden);

    List<Asignacion> findByTecnico_IdTecnico(Integer idTecnico);

    /**
     * Regla Anti-Solapamiento GiST (Sección 11.6 del informe):
     * Comprueba si el técnico ya tiene una labor en el intervalo solicitado
     * utilizando la condición matemática: (InicioExistente < FinNuevo) AND (FinExistente > InicioNuevo).
     */
    @Query("SELECT a FROM Asignacion a WHERE a.tecnico.idTecnico = :idTecnico " +
           "AND a.estadoAsignacion != 'CANCELADA' " +
           "AND (a.fechaInicio < :fin AND a.fechaFin > :inicio)")
    List<Asignacion> findOverlappingAssignments(
            @Param("idTecnico") Integer idTecnico,
            @Param("inicio") OffsetDateTime inicio,
            @Param("fin") OffsetDateTime fin
    );
}
