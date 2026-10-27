package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Integer> {
    Optional<OrdenServicio> findByCodigoOt(String codigoOt);
    List<OrdenServicio> findByEstado(String estado);
    List<OrdenServicio> findBySede_IdSede(Integer idSede);
    List<OrdenServicio> findByMaquinaria_IdMaquinaria(Integer idMaquinaria);
    List<OrdenServicio> findAllByOrderByCreadoEnDesc();

    @Query("SELECT DISTINCT o FROM OrdenServicio o " +
           "LEFT JOIN FETCH o.maquinaria m " +
           "LEFT JOIN FETCH m.modelo " +
           "LEFT JOIN FETCH m.sedeActual " +
           "LEFT JOIN FETCH m.cliente " +
           "LEFT JOIN FETCH o.sede " +
           "LEFT JOIN FETCH o.creadoPor " +
           "ORDER BY o.creadoEn DESC")
    List<OrdenServicio> findAllConRelaciones();

    @Query("SELECT o FROM OrdenServicio o " +
           "LEFT JOIN FETCH o.maquinaria m " +
           "LEFT JOIN FETCH m.modelo " +
           "LEFT JOIN FETCH m.sedeActual " +
           "LEFT JOIN FETCH m.cliente " +
           "LEFT JOIN FETCH o.sede " +
           "LEFT JOIN FETCH o.creadoPor " +
           "WHERE o.idOrden = :id")
    Optional<OrdenServicio> findByIdConRelaciones(@Param("id") Integer id);

    @Query("SELECT o FROM OrdenServicio o " +
           "LEFT JOIN FETCH o.maquinaria m " +
           "LEFT JOIN FETCH m.modelo " +
           "LEFT JOIN FETCH m.sedeActual " +
           "LEFT JOIN FETCH m.cliente " +
           "LEFT JOIN FETCH o.sede " +
           "LEFT JOIN FETCH o.creadoPor " +
           "WHERE m.idMaquinaria = :idMaquinaria ORDER BY o.creadoEn DESC")
    List<OrdenServicio> findByMaquinariaConRelaciones(@Param("idMaquinaria") Integer idMaquinaria);

    @Query("SELECT o.estado, COUNT(o) FROM OrdenServicio o GROUP BY o.estado")
    List<Object[]> contarPorEstado();

    @Query("SELECT o FROM OrdenServicio o WHERE o.cerradoEn IS NOT NULL AND o.creadoEn IS NOT NULL")
    List<OrdenServicio> findOrdenesCerradasConFechas();
}
