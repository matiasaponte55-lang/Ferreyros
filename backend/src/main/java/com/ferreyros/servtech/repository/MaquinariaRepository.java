package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Integer> {
    Optional<Maquinaria> findByNumeroSeriePin(String numeroSeriePin);
    Optional<Maquinaria> findByCodigoFlota(String codigoFlota);
    List<Maquinaria> findByEstadoOperativo(String estadoOperativo);
    List<Maquinaria> findBySedeActual_IdSede(Integer idSede);
    List<Maquinaria> findByModelo_Familia(String familia);
}
