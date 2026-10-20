package com.ferreyros.servtech.repository;

import com.ferreyros.servtech.model.RepuestoInstalado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepuestoInstaladoRepository extends JpaRepository<RepuestoInstalado, Integer> {
    List<RepuestoInstalado> findByOrden_IdOrden(Integer idOrden);
}
