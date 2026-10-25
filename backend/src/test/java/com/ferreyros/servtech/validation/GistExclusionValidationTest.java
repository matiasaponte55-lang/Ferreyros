package com.ferreyros.servtech.validation;

import com.ferreyros.servtech.dto.AsignacionRequest;
import com.ferreyros.servtech.exception.ConflictoHorarioException;
import com.ferreyros.servtech.model.Asignacion;
import com.ferreyros.servtech.model.OrdenServicio;
import com.ferreyros.servtech.model.Tecnico;
import com.ferreyros.servtech.repository.AsignacionRepository;
import com.ferreyros.servtech.repository.OrdenServicioRepository;
import com.ferreyros.servtech.repository.TecnicoRepository;
import com.ferreyros.servtech.service.AsignacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas de validación de exclusión temporal matemática GiST (Juan Zamata - QA).
 * Verifica que no se permitan asignaciones solapadas para un mismo técnico (CU-03 / RF-04).
 */
@ExtendWith(MockitoExtension.class)
public class GistExclusionValidationTest {

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private com.ferreyros.servtech.repository.UsuarioRepository usuarioRepository;

    @InjectMocks
    private AsignacionService asignacionService;

    @Test
    @DisplayName("Regla GiST RF-04: Impedir solapamiento temporal de técnico y lanzar HTTP 409 Conflict")
    void testValidacionSolapamiento_TecnicoOcupado_Lanza409() {
        Tecnico tec = new Tecnico();
        tec.setIdTecnico(5);
        tec.setEstadoDisponibilidad("DISPONIBLE");

        OrdenServicio ot = new OrdenServicio();
        ot.setIdOrden(20);
        ot.setCodigoOt("OT-2026-AQP-00020");

        Asignacion asignacionExistente = new Asignacion();
        asignacionExistente.setIdAsignacion(1);
        asignacionExistente.setTecnico(tec);
        asignacionExistente.setOrden(ot);
        asignacionExistente.setFechaInicio(OffsetDateTime.parse("2026-10-25T09:00:00Z"));
        asignacionExistente.setFechaFin(OffsetDateTime.parse("2026-10-25T11:00:00Z"));

        when(tecnicoRepository.findById(5)).thenReturn(Optional.of(tec));
        when(ordenServicioRepository.findById(20)).thenReturn(Optional.of(ot));
        when(asignacionRepository.findOverlappingAssignments(eq(5), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(List.of(asignacionExistente));

        AsignacionRequest nuevaSolapada = new AsignacionRequest();
        nuevaSolapada.setIdTecnico(5);
        nuevaSolapada.setIdOrden(20);
        nuevaSolapada.setHoraInicio("09:30"); // Solapa con 09:00 - 11:00
        nuevaSolapada.setHoraFin("10:30");

        assertThrows(ConflictoHorarioException.class, () -> {
            asignacionService.asignarCuadrilla(nuevaSolapada);
        });

        verify(asignacionRepository, never()).save(any(Asignacion.class));
    }
}
