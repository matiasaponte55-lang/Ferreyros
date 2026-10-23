package com.ferreyros.servtech.service;

import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.Tecnico;
import com.ferreyros.servtech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public List<Tecnico> listarTodos(String certificacion, String turno) {
        List<Tecnico> lista = tecnicoRepository.findAll();

        return lista.stream()
                .filter(t -> certificacion == null || certificacion.isEmpty() || "Todas".equalsIgnoreCase(certificacion) ||
                        (t.getNivelCertificacion() != null && t.getNivelCertificacion().equalsIgnoreCase(certificacion)))
                .filter(t -> turno == null || turno.isEmpty() || "Todos".equalsIgnoreCase(turno) ||
                        (t.getTurnoAsignado() != null && t.getTurnoAsignado().equalsIgnoreCase(turno)))
                .collect(Collectors.toList());
    }

    public List<Tecnico> listarDisponibles() {
        return tecnicoRepository.findByEstadoDisponibilidad("DISPONIBLE");
    }

    public Tecnico obtenerPorId(Integer id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado con ID: " + id));
    }

    public Tecnico cambiarEstado(Integer id, String nuevoEstado) {
        Tecnico tecnico = obtenerPorId(id);
        tecnico.setEstadoOperativo(nuevoEstado);
        return tecnicoRepository.save(tecnico);
    }
}
