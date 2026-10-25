package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.ActualizarEstadoOtRequest;
import com.ferreyros.servtech.dto.NuevaOrdenRequest;
import com.ferreyros.servtech.exception.HorometroInvalidoException;
import com.ferreyros.servtech.exception.RecursoNoEncontradoException;
import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para OrdenServicioService utilizando Mockito (Juan Zamata - QA).
 * Valida la regla de horómetro ascendente RF-03 y transiciones de estado de OTs.
 */
@ExtendWith(MockitoExtension.class)
public class OrdenServicioServiceTest {

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private MaquinariaRepository maquinariaRepository;

    @Mock
    private SedeRepository sedeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuditoriaEstadoOtRepository auditoriaRepository;

    @InjectMocks
    private OrdenServicioService ordenServicioService;

    private Maquinaria maquinariaMock;

    @BeforeEach
    void setUp() {
        maquinariaMock = new Maquinaria();
        maquinariaMock.setIdMaquinaria(1);
        maquinariaMock.setNumeroSeriePin("CAT797F-MIN01");
        maquinariaMock.setCodigoFlota("TK-01");
        maquinariaMock.setHorometroAcumulado(new BigDecimal("1250.0"));
        maquinariaMock.setEstadoOperativo("OPERATIVO");
    }

    @Test
    @DisplayName("RF-03: Rechazar orden si el horómetro reportado es inferior al histórico")
    void testCrearOrden_HorometroInvalido_LanzaExcepcion() {
        NuevaOrdenRequest req = new NuevaOrdenRequest();
        req.setIdMaquinaria(1);
        req.setHorometroRegistro(new BigDecimal("1200.0")); // Menor que 1250.0
        req.setTipoMantenimiento("PM1_250H");
        req.setPrioridad("ALTA");
        req.setFallaReportada("Fuga menor de refrigerante");

        when(maquinariaRepository.findById(1)).thenReturn(Optional.of(maquinariaMock));

        HorometroInvalidoException ex = assertThrows(HorometroInvalidoException.class, () -> {
            ordenServicioService.crearOrden(req);
        });

        assertTrue(ex.getMessage().contains("no puede ser inferior al acumulado"));
        verify(ordenServicioRepository, never()).save(any(OrdenServicio.class));
    }

    @Test
    @DisplayName("RF-02 / RF-03: Crear orden exitosamente con horómetro ascendente válido")
    void testCrearOrden_Exito() {
        NuevaOrdenRequest req = new NuevaOrdenRequest();
        req.setIdMaquinaria(1);
        req.setHorometroRegistro(new BigDecimal("1280.5")); // Mayor a 1250.0
        req.setTipoMantenimiento("PM2_500H");
        req.setPrioridad("CRITICA_DOWN_MACHINE");
        req.setFallaReportada("Pérdida de presión en convertidor");

        when(maquinariaRepository.findById(1)).thenReturn(Optional.of(maquinariaMock));
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenAnswer(inv -> {
            OrdenServicio o = inv.getArgument(0);
            o.setIdOrden(101);
            return o;
        });

        OrdenServicio resultado = ordenServicioService.crearOrden(req);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("1280.5"), maquinariaMock.getHorometroAcumulado());
        assertEquals("EN_MANTENIMIENTO", maquinariaMock.getEstadoOperativo());
        verify(maquinariaRepository).save(maquinariaMock);
        verify(ordenServicioRepository).save(any(OrdenServicio.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando la orden buscada por ID no existe")
    void testObtenerPorId_NoExiste_LanzaExcepcion() {
        when(ordenServicioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            ordenServicioService.obtenerPorId(999);
        });
    }

    @Test
    @DisplayName("RF-07 / RF-14: Actualizar estado de OT y registrar evento en auditoría")
    void testActualizarEstado_Exito() {
        OrdenServicio ot = new OrdenServicio();
        ot.setIdOrden(50);
        ot.setEstado("SOLICITADA");

        Usuario user = new Usuario();
        user.setUsername("supervisor@ferreyros.com.pe");

        when(ordenServicioRepository.findById(50)).thenReturn(Optional.of(ot));
        when(ordenServicioRepository.save(any(OrdenServicio.class))).thenReturn(ot);
        when(usuarioRepository.findAll()).thenReturn(java.util.List.of(user));

        ActualizarEstadoOtRequest req = new ActualizarEstadoOtRequest();
        req.setNuevoEstado("ASIGNADA");
        req.setMotivoRetrabajo("Asignación de cuadrilla de taller");

        OrdenServicio actualizada = ordenServicioService.actualizarEstado(50, req);

        assertNotNull(actualizada);
        assertEquals("ASIGNADA", actualizada.getEstado());
        verify(auditoriaRepository).save(any(AuditoriaEstadoOt.class));
    }
}
