package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.AsignacionRequest;
import com.ferreyros.servtech.exception.ConflictoHorarioException;
import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AsignacionService {

    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");

    @Autowired
    private AsignacionRepository asignacionRepository;

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaEstadoOtRepository auditoriaRepository;

    public List<Asignacion> listarTodas() {
        return asignacionRepository.findAll();
    }

    public List<Asignacion> listarPorOrden(Integer idOrden) {
        return asignacionRepository.findByOrden_IdOrden(idOrden);
    }

    public List<Asignacion> listarPorTecnico(Integer idTecnico) {
        return asignacionRepository.findByTecnico_IdTecnico(idTecnico);
    }

    /**
     * Algoritmo de Asignación y Verificación Horaria con Regla Anti-Solapamiento GiST (CU-03 / RF-04):
     * Valida que no exista colisión temporal: (InicioExistente < FinNuevo) AND (FinExistente > InicioNuevo).
     * Si colisiona, lanza ConflictoHorarioException (mapeada a HTTP 409 Conflict).
     */
    @Transactional
    public Asignacion asignarCuadrilla(AsignacionRequest request) {
        OrdenServicio orden = ordenServicioRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de servicio no encontrada con ID: " + request.getIdOrden()));

        Tecnico tecnico = tecnicoRepository.findById(request.getIdTecnico())
                .orElseThrow(() -> new RecursoNoEncontradoException("Técnico no encontrado con ID: " + request.getIdTecnico()));

        // Resolución de fechas y horas
        OffsetDateTime inicio;
        OffsetDateTime fin;

        if (request.getFechaHoraInicio() != null && request.getFechaHoraFin() != null) {
            inicio = OffsetDateTime.parse(request.getFechaHoraInicio());
            fin = OffsetDateTime.parse(request.getFechaHoraFin());
        } else if (request.getHoraInicio() != null && request.getHoraFin() != null) {
            LocalDate hoy = LocalDate.now(ZONA_PERU);
            LocalTime tInicio = LocalTime.parse(request.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime tFin = LocalTime.parse(request.getHoraFin(), DateTimeFormatter.ofPattern("HH:mm"));
            inicio = OffsetDateTime.of(hoy, tInicio, ZoneOffset.of("-05:00"));
            fin = OffsetDateTime.of(hoy, tFin, ZoneOffset.of("-05:00"));
        } else {
            // Franja por defecto de 4 horas
            inicio = OffsetDateTime.now(ZONA_PERU);
            fin = inicio.plusHours(4);
        }

        // VALIDACIÓN DE COLISIÓN HORARIA (Regla GiST)
        List<Asignacion> colisiones = asignacionRepository.findOverlappingAssignments(tecnico.getIdTecnico(), inicio, fin);
        if (!colisiones.isEmpty()) {
            Asignacion primeraColision = colisiones.get(0);
            String codigoOtConflicto = primeraColision.getOrden() != null ? primeraColision.getOrden().getCodigoOt() : "OT-PREVIA";
            throw new ConflictoHorarioException(
                    "Error 23P01: Técnico ocupado en la franja seleccionada. Ya tiene la labor " +
                    codigoOtConflicto + " asignada en dicho intervalo horario.",
                    codigoOtConflicto
            );
        }

        // Si no hay colisión, persistimos la asignación
        Asignacion nuevaAsignacion = new Asignacion(orden, tecnico, inicio, fin, Boolean.TRUE.equals(request.getEsLider()));
        Asignacion guardada = asignacionRepository.save(nuevaAsignacion);

        // Actualizamos estado de la orden
        String estadoAnterior = orden.getEstado();
        orden.setEstado("ASIGNADA");
        orden.setFechaProgramadaInicio(inicio);
        orden.setFechaProgramadaFin(fin);
        orden.setActualizadoEn(OffsetDateTime.now(ZONA_PERU));
        ordenServicioRepository.save(orden);

        // Actualizamos estado del técnico
        tecnico.setEstadoDisponibilidad("EN_SERVICIO");
        tecnico.setEstadoOperativo("EN_SERVICIO");
        tecnicoRepository.save(tecnico);

        // Bitácora Inmutable (RF-14)
        Usuario usuarioAudit = tecnico.getUsuario() != null ? tecnico.getUsuario() : usuarioRepository.findAll().get(0);
        String nombreTecnico = (tecnico.getUsuario() != null)
                ? tecnico.getUsuario().getNombres() + " " + tecnico.getUsuario().getApellidos()
                : "Técnico #" + tecnico.getIdTecnico();
        AuditoriaEstadoOt auditoria = new AuditoriaEstadoOt(
                orden,
                estadoAnterior,
                "ASIGNADA",
                usuarioAudit,
                "Asignación de cuadrilla técnica a " + nombreTecnico
        );
        auditoriaRepository.save(auditoria);

        return guardada;
    }
}
