package com.ferreyros.servtech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreyros.servtech.dto.NuevaOrdenRequest;
import com.ferreyros.servtech.exception.GlobalExceptionHandler;
import com.ferreyros.servtech.model.OrdenServicio;
import com.ferreyros.servtech.service.OrdenServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Pruebas de integración de endpoints REST con MockMvc */
@ExtendWith(MockitoExtension.class)
public class OrdenServicioControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private OrdenServicioService ordenServicioService;

    @InjectMocks
    private OrdenServicioController ordenServicioController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordenServicioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/ordenes-servicio retorna lista de OTs en formato JSON")
    void testListarOrdenes_RetornaHttp200() throws Exception {
        OrdenServicio ot = new OrdenServicio();
        ot.setIdOrden(1);
        ot.setCodigoOt("OT-2026-AQP-01001");
        ot.setEstado("SOLICITADA");

        when(ordenServicioService.listarOrdenes(null, null, null, null)).thenReturn(List.of(ot));

        mockMvc.perform(get("/api/v1/ordenes-servicio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoOt").value("OT-2026-AQP-01001"))
                .andExpect(jsonPath("$[0].estado").value("SOLICITADA"));
    }

    @Test
    @DisplayName("POST /api/v1/ordenes-servicio crea exitosamente y retorna HTTP 201 Created")
    void testCrearOrden_Valida_RetornaHttp201() throws Exception {
        NuevaOrdenRequest req = new NuevaOrdenRequest();
        req.setIdMaquinaria(1);
        req.setNumeroSeriePin("CAT-PIN-001");
        req.setHorometroRegistro(new BigDecimal("1500.50"));
        req.setTipoMantenimiento("PM2_500H");
        req.setPrioridad("ALTA");
        req.setFallaReportada("Inspección de 500 horas programada");

        OrdenServicio otCreada = new OrdenServicio();
        otCreada.setIdOrden(10);
        otCreada.setCodigoOt("OT-2026-AQP-01010");

        when(ordenServicioService.crearOrden(any(NuevaOrdenRequest.class))).thenReturn(otCreada);

        mockMvc.perform(post("/api/v1/ordenes-servicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOrden").value(10))
                .andExpect(jsonPath("$.codigoOt").value("OT-2026-AQP-01010"));
    }
}
