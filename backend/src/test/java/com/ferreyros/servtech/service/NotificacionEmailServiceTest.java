package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.NotificacionReporteResponseDto;
import com.ferreyros.servtech.dto.SolicitudReporteDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificacionEmailServiceTest {

    @InjectMocks
    private NotificacionEmailService notificacionEmailService;

    @Test
    @DisplayName("Debe procesar y registrar en auditoria la solicitud de reporte PPTX")
    void testProcesarNotificacionPptx() {
        SolicitudReporteDto dto = new SolicitudReporteDto(
                "PPTX",
                "Ferreyros_ServTech_Presentacion_2026_10_Octubre.pptx",
                "Ivan Mamani",
                "ivan.mamani@ferreyros.com.pe",
                "Sede Arequipa - CRC La Joya",
                "Reporte mensual de disponibilidad MARC 97.6%"
        );

        NotificacionReporteResponseDto respuesta = notificacionEmailService.procesarNotificacion(dto);

        assertNotNull(respuesta);
        assertTrue(respuesta.isExito());
        assertEquals("PPTX", respuesta.getTipoReporte());
        assertEquals("ivan.mamani@ferreyros.com.pe", respuesta.getCorreoDestino());
    }

    @Test
    @DisplayName("Debe procesar correctamente reportes CSV y PDF con fallback seguro")
    void testProcesarNotificacionCsvYPdf() {
        SolicitudReporteDto dtoCsv = new SolicitudReporteDto(
                "CSV",
                "Reporte_Ejecutivo_MARC_Ferreyros_2026-11-18.csv",
                "Matias Aponte",
                "maponte@ferreyros.com.pe",
                "Mina Cerro Verde",
                "Exportación de flota y horómetros acumulados"
        );

        NotificacionReporteResponseDto resCsv = notificacionEmailService.procesarNotificacion(dtoCsv);
        assertTrue(resCsv.isExito());
        assertEquals("CSV", resCsv.getTipoReporte());

        SolicitudReporteDto dtoPdf = new SolicitudReporteDto(
                "PDF",
                "Acta_Liquidacion_OT-2026-AQP-03801.pdf",
                "Jorge Zamata",
                "jzamata@ferreyros.com.pe",
                "CRC La Joya",
                "Acta técnica de liquidación y conformidad"
        );

        NotificacionReporteResponseDto resPdf = notificacionEmailService.procesarNotificacion(dtoPdf);
        assertTrue(resPdf.isExito());
        assertEquals("PDF", resPdf.getTipoReporte());
    }
}
