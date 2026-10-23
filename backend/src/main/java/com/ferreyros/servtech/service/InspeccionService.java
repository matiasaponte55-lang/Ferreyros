package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.FirmaRequest;
import com.ferreyros.servtech.dto.InspeccionRequest;
import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.OffsetDateTime;

@Service
public class InspeccionService {

    @Autowired
    private ChecklistInspeccionRepository checklistRepository;

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaEstadoOtRepository auditoriaRepository;

    public ChecklistInspeccion obtenerPorOrden(Integer idOrden) {
        return checklistRepository.findByOrden_IdOrden(idOrden)
                .orElseGet(() -> {
                    OrdenServicio orden = ordenServicioRepository.findById(idOrden)
                            .orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada"));
                    Tecnico tecnico = tecnicoRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new RecursoNoEncontradoException("No hay técnicos configurados"));
                    ChecklistInspeccion nuevo = new ChecklistInspeccion();
                    nuevo.setOrden(orden);
                    nuevo.setTecnicoResponsable(tecnico);
                    nuevo.setHorometroSalida(orden.getHorometroRegistro());
                    return checklistRepository.save(nuevo);
                });
    }

    public ChecklistInspeccion obtenerPorId(Integer id) {
        return checklistRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inspección no encontrada con ID: " + id));
    }

    @Transactional
    public ChecklistInspeccion guardarInspeccion(InspeccionRequest request) {
        ChecklistInspeccion checklist = checklistRepository.findByOrden_IdOrden(request.getIdOrden())
                .orElse(new ChecklistInspeccion());

        OrdenServicio orden = ordenServicioRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada con ID: " + request.getIdOrden()));

        Tecnico tecnico;
        if (request.getIdTecnicoResponsable() != null) {
            tecnico = tecnicoRepository.findById(request.getIdTecnicoResponsable())
                    .orElse(tecnicoRepository.findAll().get(0));
        } else {
            tecnico = tecnicoRepository.findAll().get(0);
        }

        checklist.setOrden(orden);
        checklist.setTecnicoResponsable(tecnico);
        checklist.setHorometroSalida(request.getHorometroSalida() != null ? request.getHorometroSalida() : orden.getHorometroRegistro());
        checklist.setAtsAprobado(Boolean.TRUE.equals(request.getAtsAprobado()));
        checklist.setLotoBloqueado(Boolean.TRUE.equals(request.getLotoBloqueado()));
        checklist.setEppCompleto(Boolean.TRUE.equals(request.getEppCompleto()));
        checklist.setAreaDemarcada(Boolean.TRUE.equals(request.getAreaDemarcada()));

        if (request.getEvaluacionMotor() != null) checklist.setEvaluacionMotor(request.getEvaluacionMotor());
        if (request.getEvaluacionHidraulico() != null) checklist.setEvaluacionHidraulico(request.getEvaluacionHidraulico());
        if (request.getEvaluacionTransmision() != null) checklist.setEvaluacionTransmision(request.getEvaluacionTransmision());
        if (request.getEvaluacionTrenRodaje() != null) checklist.setEvaluacionTrenRodaje(request.getEvaluacionTrenRodaje());
        if (request.getObservaciones() != null) checklist.setObservaciones(request.getObservaciones());

        // Si incluye firma digital en este mismo guardado:
        if (request.getFirmaBase64() != null && !request.getFirmaBase64().trim().isEmpty()) {
            checklist.setFirmaBase64(request.getFirmaBase64());
            checklist.setFirmanteNombre(request.getFirmanteNombre());
            checklist.setFirmanteDni(request.getFirmanteDni());
            checklist.setFirmanteCargo(request.getFirmanteCargo());
            checklist.setHashCriptografico(calcularSha256(request.getFirmaBase64() + orden.getCodigoOt() + System.currentTimeMillis()));
            checklist.setFechaCierre(OffsetDateTime.now());
        }

        ChecklistInspeccion guardada = checklistRepository.save(checklist);

        // Actualizamos estado de la orden a EN_REPARACION o CONTROL_CALIDAD
        if ("SOLICITADA".equalsIgnoreCase(orden.getEstado()) || "ASIGNADA".equalsIgnoreCase(orden.getEstado())) {
            orden.setEstado("EN_DIAGNOSTICO");
            ordenServicioRepository.save(orden);
        }

        return guardada;
    }

    /**
     * Firma digital de conformidad y no repudio en Canvas táctil (CU-06 / RF-09).
     * Genera digest criptográfico SHA-256 e inmutabiliza la liquidación del servicio.
     */
    @Transactional
    public ChecklistInspeccion firmarInspeccion(Integer idOrden, FirmaRequest request) {
        ChecklistInspeccion checklist = obtenerPorOrden(idOrden);
        OrdenServicio orden = checklist.getOrden();

        checklist.setFirmanteNombre(request.getFirmanteNombre());
        checklist.setFirmanteDni(request.getFirmanteDni());
        checklist.setFirmanteCargo(request.getFirmanteCargo());
        checklist.setFirmaBase64(request.getFirmaBase64());
        checklist.setFechaCierre(OffsetDateTime.now());

        // Cómputo de Digest Criptográfico SHA-256
        String semilla = request.getFirmaBase64() + "|" + orden.getCodigoOt() + "|" + request.getFirmanteDni() + "|" + OffsetDateTime.now();
        checklist.setHashCriptografico(calcularSha256(semilla));

        ChecklistInspeccion guardada = checklistRepository.save(checklist);

        // Transiciona la orden a LIQUIDADA y libera la maquinaria
        String estadoAnterior = orden.getEstado();
        orden.setEstado("LIQUIDADA");
        orden.setCerradoEn(OffsetDateTime.now());
        if (orden.getMaquinaria() != null) {
            orden.getMaquinaria().setEstadoOperativo("OPERATIVO");
            if (checklist.getHorometroSalida() != null) {
                orden.getMaquinaria().setHorometroAcumulado(checklist.getHorometroSalida());
            }
        }
        ordenServicioRepository.save(orden);

        // Bitácora de Auditoría (RF-14)
        Usuario usuarioAudit = !usuarioRepository.findAll().isEmpty() ? usuarioRepository.findAll().get(0) : null;
        if (usuarioAudit != null) {
            AuditoriaEstadoOt auditoria = new AuditoriaEstadoOt(
                    orden,
                    estadoAnterior,
                    "CERRADA",
                    usuarioAudit,
                    "Firma de conformidad técnica en terreno sellada con SHA-256 por " + request.getFirmanteNombre()
            );
            auditoriaRepository.save(auditoria);
        }

        return guardada;
    }

    private String calcularSha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        }
    }
}
