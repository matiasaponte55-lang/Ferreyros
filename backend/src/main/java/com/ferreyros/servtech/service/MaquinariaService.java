package com.ferreyros.servtech.service;

import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.Maquinaria;
import com.ferreyros.servtech.repository.MaquinariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaquinariaService {

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    public List<Maquinaria> listarTodas(String familia, String estado, String buscar) {
        List<Maquinaria> lista = maquinariaRepository.findAll();

        return lista.stream()
                .filter(m -> familia == null || familia.isEmpty() || "Todas".equalsIgnoreCase(familia) ||
                        (m.getModelo() != null && familia.equalsIgnoreCase(m.getModelo().getFamilia())))
                .filter(m -> estado == null || estado.isEmpty() || "Todos".equalsIgnoreCase(estado) ||
                        (m.getEstadoOperativo() != null && estado.equalsIgnoreCase(m.getEstadoOperativo())))
                .filter(m -> {
                    if (buscar == null || buscar.trim().isEmpty()) return true;
                    String q = buscar.toLowerCase().trim();
                    boolean matchCodigo = m.getCodigoFlota() != null && m.getCodigoFlota().toLowerCase().contains(q);
                    boolean matchPin = m.getNumeroSeriePin() != null && m.getNumeroSeriePin().toLowerCase().contains(q);
                    boolean matchModelo = m.getModelo() != null && m.getModelo().getNombreComercial().toLowerCase().contains(q);
                    boolean matchCliente = m.getCliente() != null && m.getCliente().getRazonSocial().toLowerCase().contains(q);
                    return matchCodigo || matchPin || matchModelo || matchCliente;
                })
                .collect(Collectors.toList());
    }

    public Maquinaria obtenerPorId(Integer id) {
        return maquinariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Maquinaria no encontrada con ID: " + id));
    }

    public Maquinaria obtenerPorPin(String pin) {
        return maquinariaRepository.findByNumeroSeriePin(pin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Maquinaria no encontrada con serie PIN: " + pin));
    }

    public Maquinaria guardar(Maquinaria maquinaria) {
        if (maquinaria.getCreadoEn() == null) {
            maquinaria.setCreadoEn(OffsetDateTime.now());
        }
        maquinaria.setActualizadoEn(OffsetDateTime.now());
        return maquinariaRepository.save(maquinaria);
    }
}
