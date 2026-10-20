package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.ModeloCatMaestro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeloCatMaestroRepository extends JpaRepository<ModeloCatMaestro, Integer> {
    Optional<ModeloCatMaestro> findByCodigoModelo(String codigoModelo);
}
