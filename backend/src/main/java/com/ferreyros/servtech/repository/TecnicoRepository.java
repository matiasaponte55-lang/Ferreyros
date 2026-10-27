package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    Optional<Tecnico> findByCodigoEmpleado(String codigoEmpleado);
    Optional<Tecnico> findByUsuario_Username(String username);
    List<Tecnico> findBySedeBase_IdSede(Integer idSede);
    List<Tecnico> findByEstadoDisponibilidad(String estadoDisponibilidad);
    List<Tecnico> findByNivelCertificacion(String nivelCertificacion);

    @Query("SELECT DISTINCT t FROM Tecnico t LEFT JOIN FETCH t.usuario LEFT JOIN FETCH t.sedeBase")
    List<Tecnico> findAllConRelaciones();

    @Query("SELECT DISTINCT t FROM Tecnico t LEFT JOIN FETCH t.usuario LEFT JOIN FETCH t.sedeBase WHERE t.estadoDisponibilidad = 'DISPONIBLE' OR t.estadoOperativo = 'DISPONIBLE'")
    List<Tecnico> findDisponiblesConRelaciones();

    @Query("SELECT t FROM Tecnico t LEFT JOIN FETCH t.usuario LEFT JOIN FETCH t.sedeBase WHERE t.idTecnico = :id")
    Optional<Tecnico> findByIdConRelaciones(@Param("id") Integer id);
}
