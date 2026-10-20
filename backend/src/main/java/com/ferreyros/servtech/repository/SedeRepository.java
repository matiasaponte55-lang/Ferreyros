package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    Optional<Sede> findByCodigoSede(String codigoSede);
}
