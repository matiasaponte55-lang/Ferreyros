-- =========================================================================
-- TRIGGERS DE INTEGRIDAD RELACIONAL Y AUDITORÍA INMUTABLE EN SUPABASE
-- RF-03: Regla de Horómetro Ascendente | RF-14: Bitácora Inmutable
-- Rama: feature/trigger-horometro-ascendente | Integrante: Matias Aponte
-- =========================================================================

-- 1. TABLA FORENSE DE AUDITORÍA INMUTABLE (RF-14)
CREATE TABLE IF NOT EXISTS auditoria_estados_ot (
    id_auditoria BIGSERIAL PRIMARY KEY,
    id_orden INT NOT NULL REFERENCES ordenes_servicio(id_orden) ON DELETE CASCADE,
    estado_anterior VARCHAR(30),
    estado_nuevo VARCHAR(30) NOT NULL,
    cambiado_por UUID REFERENCES usuarios(id_usuario),
    motivo_retrabajo TEXT,
    fecha_cambio TIMESTAMPTZ DEFAULT NOW(),
    direccion_ip VARCHAR(45) DEFAULT '127.0.0.1'
);

-- -------------------------------------------------------------------------
-- 2. TRIGGER 1: VALIDACIÓN DE HORÓMETRO ASCENDENTE (RF-03)
-- Impide fraudes en contratos MARC bloqueando ingresos con horómetro inferior
-- -------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_validar_horometro_ascendente()
RETURNS TRIGGER AS $$
DECLARE
    v_horometro_acumulado NUMERIC(10,2);
    v_codigo_flota VARCHAR(30);
BEGIN
    SELECT horometro_acumulado, codigo_flota
    INTO v_horometro_acumulado, v_codigo_flota
    FROM maquinarias
    WHERE id_maquinaria = NEW.id_maquinaria;

    IF NEW.horometro_registro < v_horometro_acumulado THEN
        RAISE EXCEPTION 'Violación de Integridad RF-03: El horómetro reportado (%) para el equipo % no puede ser inferior al horómetro verificado en telemetría (%).',
            NEW.horometro_registro, v_codigo_flota, v_horometro_acumulado
            USING ERRCODE = 'check_violation';
    END IF;

    -- Si el horómetro es mayor, actualiza automáticamente el activo
    UPDATE maquinarias
    SET horometro_acumulado = NEW.horometro_registro,
        actualizado_en = NOW()
    WHERE id_maquinaria = NEW.id_maquinaria;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_horometro_ot ON ordenes_servicio;
CREATE TRIGGER trg_check_horometro_ot
    BEFORE INSERT OR UPDATE OF horometro_registro ON ordenes_servicio
    FOR EACH ROW
    EXECUTE FUNCTION fn_validar_horometro_ascendente();

-- -------------------------------------------------------------------------
-- 3. TRIGGER 2: AUDITORÍA INMUTABLE DE CAMBIOS DE ESTADO (RF-14)
-- Registra cada transición de estado y motivos de retrabajo (QC Rejected)
-- -------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_auditar_cambio_estado()
RETURNS TRIGGER AS $$
BEGIN
    IF (OLD.estado IS DISTINCT FROM NEW.estado) THEN
        INSERT INTO auditoria_estados_ot (
            id_orden,
            estado_anterior,
            estado_nuevo,
            cambiado_por,
            motivo_retrabajo,
            fecha_cambio
        ) VALUES (
            NEW.id_orden,
            OLD.estado,
            NEW.estado,
            NEW.id_creador,
            'Transición auditada en ciclo de vida OT',
            NOW()
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_auditar_estados ON ordenes_servicio;
CREATE TRIGGER trg_auditar_estados
    AFTER UPDATE OF estado ON ordenes_servicio
    FOR EACH ROW
    EXECUTE FUNCTION fn_auditar_cambio_estado();
