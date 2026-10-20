package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Integer> {
    Optional<Competencia> findByCodigoCat(String codigoCat);
    List<Competencia> findByCategoria(String categoria);
}
