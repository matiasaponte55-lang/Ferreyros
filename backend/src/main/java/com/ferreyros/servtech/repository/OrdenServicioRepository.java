package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
