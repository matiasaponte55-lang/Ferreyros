package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.TecnicoCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoCompetenciaRepository extends JpaRepository<TecnicoCompetencia, TecnicoCompetencia.TecnicoCompetenciaId> {
    List<TecnicoCompetencia> findByTecnico_IdTecnico(Integer idTecnico);
    List<TecnicoCompetencia> findByCompetencia_IdCompetencia(Integer idCompetencia);
}
