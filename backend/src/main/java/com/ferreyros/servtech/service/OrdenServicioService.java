package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.ActualizarEstadoOtRequest;
import com.ferreyros.servtech.dto.NuevaOrdenRequest;
import com.ferreyros.servtech.exception.HorometroInvalidoException;
import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenServicioService {

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaEstadoOtRepository auditoriaRepository;

    public List<OrdenServicio> listarOrdenes(String estado, Integer idSede, String prioridad, String buscar) {
        List<OrdenServicio> lista = ordenServicioRepository.findAllConRelaciones();

        return lista.stream()
                .filter(o -> estado == null || estado.isEmpty() || "Todas".equalsIgnoreCase(estado) ||
                        o.getEstado().equalsIgnoreCase(estado))
                .filter(o -> idSede == null || (o.getSede() != null && o.getSede().getIdSede().equals(idSede)))
                .filter(o -> prioridad == null || prioridad.isEmpty() || "Todas".equalsIgnoreCase(prioridad) ||
                        o.getPrioridad().equalsIgnoreCase(prioridad))
                .filter(o -> {
                    if (buscar == null || buscar.trim().isEmpty()) return true;
                    String q = buscar.toLowerCase().trim();
                    boolean matchCodigo = o.getCodigoOt() != null && o.getCodigoOt().toLowerCase().contains(q);
                    boolean matchFalla = o.getFallaReportada() != null && o.getFallaReportada().toLowerCase().contains(q);
                    boolean matchPin = o.getMaquinaria() != null && o.getMaquinaria().getNumeroSeriePin() != null && o.getMaquinaria().getNumeroSeriePin().toLowerCase().contains(q);
                    boolean matchFlota = o.getMaquinaria() != null && o.getMaquinaria().getCodigoFlota() != null && o.getMaquinaria().getCodigoFlota().toLowerCase().contains(q);
                    return matchCodigo || matchFalla || matchPin || matchFlota;
                })
                .collect(Collectors.toList());
    }

    public OrdenServicio obtenerPorId(Integer id) {
        return ordenServicioRepository.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de trabajo no encontrada con ID: " + id));
    }

    public List<OrdenServicio> listarPorMaquinaria(Integer idMaquinaria) {
        return ordenServicioRepository.findByMaquinariaConRelaciones(idMaquinaria);
    }

    /* ODT Valida el horometro y actualiza el acumulado de la maquinaria */
    @Transactional
    @CacheEvict(value = "dashboardMetricas", allEntries = true)
    public OrdenServicio crearOrden(NuevaOrdenRequest request) {
        Maquinaria maquinaria;
        if (request.getIdMaquinaria() != null) {
            maquinaria = maquinariaRepository.findById(request.getIdMaquinaria())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Maquinaria no encontrada"));
        } else if (request.getNumeroSeriePin() != null) {
            maquinaria = maquinariaRepository.findByNumeroSeriePin(request.getNumeroSeriePin())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Maquinaria con PIN " + request.getNumeroSeriePin() + " no encontrada"));
        } else {
            throw new IllegalArgumentException("Debe especificar la maquinaria o su número de serie PIN");
        }

        // VALIDACIÓN DE HORÓMETRO ASCENDENTE 
        if (request.getHorometroRegistro() == null) {
            throw new HorometroInvalidoException("El horómetro es un campo obligatorio");
        }
        if (request.getHorometroRegistro().compareTo(maquinaria.getHorometroAcumulado()) < 0) {
            throw new HorometroInvalidoException(
                    "Violación de Integridad en Horómetro [Equipo PIN: " + maquinaria.getNumeroSeriePin() +
                    "]: El valor reportado (" + request.getHorometroRegistro() +
                    ") no puede ser inferior al acumulado histórico verificado (" + maquinaria.getHorometroAcumulado() + ")"
            );
        }

        // Actualización del horómetro acumulado en el equipo
        maquinaria.setHorometroAcumulado(request.getHorometroRegistro());
        maquinaria.setEstadoOperativo("EN_MANTENIMIENTO");
        maquinaria.setActualizadoEn(OffsetDateTime.now());
        maquinariaRepository.save(maquinaria);

        // Determinación de la sede
        Sede sede = null;
        if (request.getIdSede() != null) {
            sede = sedeRepository.findById(request.getIdSede()).orElse(maquinaria.getSedeActual());
        } else {
            sede = maquinaria.getSedeActual();
        }

        // Determinación del usuario creador
        Usuario creador = null;
        if (request.getCreadoPorUsername() != null) {
            creador = usuarioRepository.findByUsername(request.getCreadoPorUsername()).orElse(null);
        }
        if (creador == null && !usuarioRepository.findAll().isEmpty()) {
            creador = usuarioRepository.findAll().get(0);
        }

        // Mapeo seguro de tipo de mantenimiento y prioridad a las restricciones CHECK de la BD
        String tipoMant = request.getTipoMantenimiento();
        if (tipoMant == null || tipoMant.isEmpty()) tipoMant = "PM2_500H";
        if (tipoMant.equals("PM1")) tipoMant = "PM1_250H";
        if (tipoMant.equals("PM2")) tipoMant = "PM2_500H";
        if (tipoMant.equals("PM3")) tipoMant = "PM3_1000H";
        if (tipoMant.equals("PM4")) tipoMant = "PM4_2000H";

        String prioridad = request.getPrioridad();
        if (prioridad == null || prioridad.isEmpty()) prioridad = "MEDIA";
        if (prioridad.contains("CRITICA")) prioridad = "CRITICA_DOWN_MACHINE";

        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        int aleatorio = 1000 + secureRandom.nextInt(9000);
        String codigoOt = "OT-2026-AQP-" + String.format("%05d", aleatorio);

        OrdenServicio orden = new OrdenServicio();
        orden.setCodigoOt(codigoOt);
        orden.setMaquinaria(maquinaria);
        orden.setSede(sede);
        orden.setTipoMantenimiento(tipoMant);
        orden.setPrioridad(prioridad);
        orden.setFallaReportada(request.getFallaReportada() != null ? request.getFallaReportada() : "Mantenimiento preventivo programado");
        orden.setHorometroRegistro(request.getHorometroRegistro());
        orden.setEstado("SOLICITADA");
        orden.setCreadoPor(creador);
        orden.setCreadoEn(OffsetDateTime.now());
        orden.setActualizadoEn(OffsetDateTime.now());

        OrdenServicio guardada = ordenServicioRepository.save(orden);

        // Bitácora Inmutable de Auditoría 
        if (creador != null) {
            AuditoriaEstadoOt auditoria = new AuditoriaEstadoOt(
                    guardada,
                    "NINGUNO",
                    "SOLICITADA",
                    creador,
                    "Apertura inicial de orden de servicio"
            );
            auditoriaRepository.save(auditoria);
        }

        return guardada;
    }

    /* Transición controlada de ciclo de vida de la OT   */
    @Transactional
    @CacheEvict(value = "dashboardMetricas", allEntries = true)
    public OrdenServicio actualizarEstado(Integer idOrden, ActualizarEstadoOtRequest request) {
        OrdenServicio orden = obtenerPorId(idOrden);
        String estadoAnterior = orden.getEstado();
        String nuevoEstado = request.getNuevoEstado();

        if (nuevoEstado == null || nuevoEstado.equalsIgnoreCase(estadoAnterior)) {
            return orden;
        }

        // Mapeo seguro si llega LIQUIDADA
        if ("LIQUIDADA".equalsIgnoreCase(nuevoEstado)) {
            nuevoEstado = "CERRADA";
        }

        // Validación de Retrabajo Mandatorio 
        if ("CONTROL_CALIDAD".equalsIgnoreCase(estadoAnterior) && "EN_REPARACION".equalsIgnoreCase(nuevoEstado)) {
            if (request.getMotivoRetrabajo() == null || request.getMotivoRetrabajo().trim().length() < 10) {
                throw new IllegalArgumentException("Para registrar un retrabajo desde Control de Calidad es obligatorio ingresar un motivo de al menos 10 caracteres.");
            }
        }

        orden.setEstado(nuevoEstado);
        orden.setActualizadoEn(OffsetDateTime.now());

        if ("CERRADA".equalsIgnoreCase(nuevoEstado)) {
            orden.setCerradoEn(OffsetDateTime.now());
            if (orden.getMaquinaria() != null) {
                orden.getMaquinaria().setEstadoOperativo("OPERATIVO");
                maquinariaRepository.save(orden.getMaquinaria());
            }
        }

        OrdenServicio actualizada = ordenServicioRepository.save(orden);

        // Registro de Auditoría Inmutable 
        Usuario usuarioCambio = null;
        if (request.getCambiadoPor() != null) {
            usuarioCambio = usuarioRepository.findByUsername(request.getCambiadoPor()).orElse(null);
        }
        if (usuarioCambio == null && !usuarioRepository.findAll().isEmpty()) {
            usuarioCambio = usuarioRepository.findAll().get(0);
        }

        if (usuarioCambio != null) {
            AuditoriaEstadoOt auditoria = new AuditoriaEstadoOt(
                    actualizada,
                    estadoAnterior,
                    nuevoEstado,
                    usuarioCambio,
                    request.getMotivoRetrabajo() != null ? request.getMotivoRetrabajo() : "Transición autorizada de estado de OT"
            );
            auditoriaRepository.save(auditoria);
        }

        return actualizada;
    }
}
