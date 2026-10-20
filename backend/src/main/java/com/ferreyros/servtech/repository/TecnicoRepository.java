package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
